import 'package:flutter_codesdancing/src/flutter_boost/container/container_manager.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_codesdancing/src/flutter_boost/flutter_boost.dart';
import 'package:flutter_codesdancing/src/flutter_boost/container/container_coordinator.dart';

final GlobalKey scaffoldKey = GlobalKey();

class FirstRouteWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('First Route'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            RaisedButton(
              child: Text('First'),
              onPressed: () {
                print("open second page!");
                FlutterBoost.singleton.open("second").then((Map value) {
                  print(
                      "call me when page is finished. did recieve second route result $value");
                });
              },
            ),
          ],
        ),
      ),
    );
  }
}

class SecondRouteWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Second Route'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            RaisedButton(
              child: Text('Second'),
              onPressed: () {
                print("open second page!");
                FlutterBoost.singleton.open("second").then((Map value) {
                  print(
                      "call me when page is finished. did recieve second route result $value");
                });
              },
            ),
          ],
        ),
      ),
    );
  }
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();

    FlutterBoost.singleton.registerPageBuilders({
      'first': (pageName, params, _) => FirstRouteWidget(),
      'second': (pageName, params, _) => SecondRouteWidget(),
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        title: 'Flutter Boost example',
        key: scaffoldKey,
        builder: (BuildContext context, Widget child) {
          assert(child is Navigator, 'child must be Navigator, what is wrong?');

          final BoostContainerManager manager = BoostContainerManager(
            initNavigator: child,
          );

          return manager;
        },
        home: Container());
  }
}

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  testWidgets('test iOS edge swipe then drop back at starting point works',
      (WidgetTester tester) async {
    await tester.pumpWidget(MyApp());

    expect(find.text('First'), findsNothing);
  });

  group(
    'Try to get the ContainerManagerState in the ancestor node',
    () {
      testWidgets(
        'through the `BoostContainerManager.of(context)` method',
        (WidgetTester tester) async {
          var builderContext;

          FlutterBoost.singleton.registerPageBuilders({
            'context': (pageName, params, _) => Builder(
                  builder: (context) {
                    return FloatingActionButton(
                      onPressed: () {
                        builderContext = context;
                      },
                    );
                  },
                ),
          });

          await tester.pumpWidget(
            MaterialApp(
              builder: FlutterBoost.init(),
              home: Container(),
            ),
          );

          //open context page
          ContainerCoordinator.singleton
              .nativeContainerDidShow("context", {}, "1000000");

          await tester.pump(Duration(seconds: 1));

          expect(find.byType(FloatingActionButton), findsOneWidget);

          //get the context of the Builder
          await tester.tap(find.byType(FloatingActionButton));

          final isFind = BoostContainerManager.of(builderContext) != null;

          expect(isFind, true,
              reason: '`BoostContainerManager.of` should be able to '
                  'find `ContainerManagerState` in `FlutterBoost.init()` based on the context of the `Builder`');
        },
      );

//      testWidgets(
//        'through the `BoostContainerManager.of(context)` method',
//        (WidgetTester tester) async {
//          var builderContext;
//
//          await tester.pumpWidget(
//            MaterialApp(
//              home: Builder(
//                builder: (context) {
//                  return FloatingActionButton(
//                    onPressed: () {
//                      builderContext = context;
//                    },
//                  );
//                },
//              ),
//            ),
//          );
//
//          expect(find.byType(FloatingActionButton), findsOneWidget);
//
//          //get the context of the Builder
//          await tester.tap(find.byType(FloatingActionButton));
//
//          expect(BoostContainerManager.of(builderContext), isAssertionError);
//        },
//      );

      testWidgets(
        'through the `BoostContainerManager.tryOf(context)` method',
        (WidgetTester tester) async {
          var builderContext;

          FlutterBoost.singleton.registerPageBuilders({
            'context': (pageName, params, _) => Builder(
                  builder: (context) {
                    return FloatingActionButton(
                      onPressed: () {
                        builderContext = context;
                      },
                    );
                  },
                ),
          });

          await tester.pumpWidget(
            MaterialApp(
              builder: FlutterBoost.init(),
              home: Container(),
            ),
          );

          //open context page
          ContainerCoordinator.singleton
              .nativeContainerDidShow("context", {}, "1000000");

          await tester.pump(Duration(seconds: 1));

          expect(find.byType(FloatingActionButton), findsOneWidget);

          //get the context of the Builder
          await tester.tap(find.byType(FloatingActionButton));

          final isFind = BoostContainerManager.tryOf(builderContext) != null;

          expect(isFind, true,
              reason: '`BoostContainerManager.tryOf` should be able to '
                  'find `ContainerManagerState` in `FlutterBoost.init()` based on the context of the `Builder`');
        },
      );
    },
  );

  group('ContainerManagerState', () {
    testWidgets(
      'containerCounts should change based on the number of pages',
      (WidgetTester tester) async {
        var builderContext;

        FlutterBoost.singleton.registerPageBuilders({
          'context': (pageName, params, _) => Builder(
                builder: (context) {
                  return FloatingActionButton(
                    onPressed: () {
                      builderContext = context;
                    },
                  );
                },
              ),
        });

        await tester.pumpWidget(
          MaterialApp(
            builder: FlutterBoost.init(),
            home: Container(),
          ),
        );

        //open first context page
        ContainerCoordinator.singleton
            .nativeContainerDidShow("context", {}, "1000000");

        await tester.pump(Duration(seconds: 1));

        //get the context of the Builder
        await tester.tap(find.byType(FloatingActionButton));

        final containerManagerState = BoostContainerManager.of(builderContext);

        expect(containerManagerState.containerCounts, 1,
            reason: '1 page shown');

        //open second context page
        ContainerCoordinator.singleton
            .nativeContainerDidShow("context", {}, "2000000");

        await tester.pump(Duration(seconds: 1));

        expect(containerManagerState.containerCounts, 2,
            reason: '2 page shown');

        //pop second context page
        containerManagerState.pop();

        await tester.pump(Duration(seconds: 1));

        expect(containerManagerState.containerCounts, 1,
            reason: 'second context page closed, Only one page left');

        //pop last context page
        containerManagerState.pop();

        await tester.pump(Duration(seconds: 1));

        expect(containerManagerState.containerCounts, 0,
            reason: 'last context page closed, no page left');
      },
    );
  });
}
