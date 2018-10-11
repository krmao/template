// Generated file. Do not edit.
def scriptFile = new File(getClass().protectionDomain.codeSource.location.path)
def flutterProjectRoot = scriptFile.parentFile.parentFile
def pluginsFile = new File(flutterProjectRoot, "flutter_mixed/.flutter-plugins")
def flutterPath = new File(flutterProjectRoot, "flutter_mixed/.android/Flutter")

print("scriptFile:$scriptFile\n")
print("pluginsFile:$pluginsFile\n")
print("flutterPath:$flutterPath\n\n")

gradle.include ':flutter'
gradle.project(':flutter').projectDir = flutterPath

def plugins = new Properties()
if (pluginsFile.exists()) {
    pluginsFile.withReader('UTF-8') { reader -> plugins.load(reader) }
}

plugins.each { name, path ->
    def pluginDirectory = flutterProjectRoot.toPath().resolve(path).resolve('android').toFile()
    gradle.include ":$name"
    gradle.project(":$name").projectDir = pluginDirectory
}

gradle.getGradle().projectsLoaded { g ->
    g.rootProject.afterEvaluate { p ->
        p.subprojects { sp ->
            if (sp.name != 'flutter') {
                sp.evaluationDependsOn(':flutter')
            }
        }
    }
}