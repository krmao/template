# Install pods needed to embed Flutter application, Flutter engine, and plugins
# from the host application Podfile.
#
# @example
#   target 'MyApp' do
#     install_all_flutter_pods 'my_flutter'
#   end
# @param [String] flutter_application_path Path of the root directory of the Flutter module.
#                                          Optional, defaults to two levels up from the directory of this script.
#                                          MyApp/my_flutter/.ios/Flutter/../..
def install_all_flutter_pods(flutter_application_path = nil)
  flutter_application_path ||= File.join('..', '..')
  install_flutter_engine_pod
  install_flutter_plugin_pods(flutter_application_path)
  install_flutter_application_pod(flutter_application_path)
end

# Install Flutter engine pod.
#
# @example
#   target 'MyApp' do
#     install_flutter_engine_pod
#   end
def install_flutter_engine_pod
  engine_dir = File.join(__dir__, 'engine')
  if !File.exist?(engine_dir)
    # Copy the debug engine to have something to link against if the xcode backend script has not run yet.
    # CocoaPods will not embed the framework on pod install (before any build phases can generate) if the dylib does not exist.
    debug_framework_dir = File.join(flutter_root, 'bin', 'cache', 'artifacts', 'engine', 'ios')
    FileUtils.mkdir_p(engine_dir)
    FileUtils.cp_r(File.join(debug_framework_dir, 'Flutter.framework'), engine_dir)
    FileUtils.cp(File.join(debug_framework_dir, 'Flutter.podspec'), engine_dir)
  end

  # Keep pod path relative so it can be checked into Podfile.lock.
  # Process will be run from project directory.
  engine_pathname = Pathname.new engine_dir
  project_directory_pathname = Pathname.new Dir.pwd
  relative = engine_pathname.relative_path_from project_directory_pathname

  pod 'Flutter', :path => relative.to_s, :inhibit_warnings => true
end

# Install Flutter plugin pods.
#
# @example
#   target 'MyApp' do
#     install_flutter_plugin_pods 'my_flutter'
#   end
# @param [String] flutter_application_path Path of the root directory of the Flutter module.
#                                          Optional, defaults to two levels up from the directory of this script.
#                                          MyApp/my_flutter/.ios/Flutter/../..
def install_flutter_plugin_pods(flutter_application_path)
  flutter_application_path ||= File.join('..', '..')

  # Keep pod path relative so it can be checked into Podfile.lock.
  # Process will be run from project directory.
  current_directory_pathname = Pathname.new __dir__
  project_directory_pathname = Pathname.new Dir.pwd
  relative = current_directory_pathname.relative_path_from project_directory_pathname
  pod 'FlutterPluginRegistrant', :path => File.join(relative, 'FlutterPluginRegistrant'), :inhibit_warnings => true

  symlinks_dir = File.join(relative, '.symlinks')
  FileUtils.mkdir_p(symlinks_dir)
  plugin_pods = parse_KV_file(File.join(flutter_application_path, '.flutter-plugins'))
  plugin_pods.map do |r|
    symlink = File.join(symlinks_dir, r[:name])
    FileUtils.rm_f(symlink)
    File.symlink(r[:path], symlink)
    pod r[:name], :path => File.join(symlink, 'ios'), :inhibit_warnings => true
  end
end

# Install Flutter application pod.
#
# @example
#   target 'MyApp' do
#     install_flutter_application_pod '../flutter_settings_repository'
#   end
# @param [String] flutter_application_path Path of the root directory of the Flutter module.
#                                          Optional, defaults to two levels up from the directory of this script.
#                                          MyApp/my_flutter/.ios/Flutter/../..
def install_flutter_application_pod(flutter_application_path)
  app_framework_dir = File.join(__dir__, 'App.framework')
  app_framework_dylib = File.join(app_framework_dir, 'App')
  if !File.exist?(app_framework_dylib)
    # Fake an App.framework to have something to link against if the xcode backend script has not run yet.
    # CocoaPods will not embed the framework on pod install (before any build phases can run) if the dylib does not exist.
    # Create a dummy dylib.
    FileUtils.mkdir_p(app_framework_dir)
    `echo "static const int Moo = 88;" | xcrun clang -x c -dynamiclib -o "#{app_framework_dylib}" -`
  end

  # Keep pod and script phase paths relative so they can be checked into source control.
  # Process will be run from project directory.
  current_directory_pathname = Pathname.new __dir__
  project_directory_pathname = Pathname.new Dir.pwd
  relative = current_directory_pathname.relative_path_from project_directory_pathname
  pod 'my_flutter', :path => relative.to_s, :inhibit_warnings => true

  flutter_export_environment_path = File.join('${SRCROOT}', relative, 'flutter_export_environment.sh');
  script_phase :name => 'Run Flutter Build Script',
    :script => "set -e\nset -u\nsource \"#{flutter_export_environment_path}\"\n\"$FLUTTER_ROOT\"/packages/flutter_tools/bin/xcode_backend.sh build",
    :input_files => [
      File.join('${SRCROOT}', flutter_application_path, '.metadata'),
      File.join('${SRCROOT}', relative, 'App.framework', 'App'),
      File.join('${SRCROOT}', relative, 'engine', 'Flutter.framework', 'Flutter'),
      flutter_export_environment_path
    ],
    :execution_position => :before_compile
end

def parse_KV_file(file, separator='=')
  file_abs_path = File.expand_path(file)
  if !File.exists? file_abs_path
    return [];
  end
  pods_array = []
  skip_line_start_symbols = ["#", "/"]
  File.foreach(file_abs_path) { |line|
    next if skip_line_start_symbols.any? { |symbol| line =~ /^\s*#{symbol}/ }
    plugin = line.split(pattern=separator)
    if plugin.length == 2
      podname = plugin[0].strip()
      path = plugin[1].strip()
      podpath = File.expand_path("#{path}", file_abs_path)
      pods_array.push({:name => podname, :path => podpath});
     else
      puts "Invalid plugin specification: #{line}"
    end
  }
  return pods_array
end

def flutter_root
  generated_xcode_build_settings = parse_KV_file(File.join(__dir__, 'Generated.xcconfig'))
  if generated_xcode_build_settings.empty?
    puts "Generated.xcconfig must exist. Make sure `flutter pub get` is executed in the Flutter module."
    exit
  end
  generated_xcode_build_settings.map { |p|
    if p[:name] == 'FLUTTER_ROOT'
      return p[:path]
    end
  }
end
