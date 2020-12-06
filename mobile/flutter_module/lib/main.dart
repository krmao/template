import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter_module/libraries/codesdancing_bridge/codesdancing_bridge.dart';
import 'package:flutter_module/modules/bridge/bridge_widget.dart';

import 'settings/router/flutter_router.dart';

void main() {
  debugPaintSizeEnabled = false;

  FlutterError.onError = (FlutterErrorDetails details) {
    Zone.current.handleUncaughtError(details.exception, details.stack);
  };

  runZoned(
    () => runApp(
      App(
        enableSafeArea: false,
        statusBarColor: Constants.DEFAULT_STATUS_BAR_COLOR,
        child: PageWidget(state: BridgeWidgetState()),
        onInitStateCallback: () {
          FlutterRouter.initialize();
        },
      ),
    ),
    zoneSpecification: ZoneSpecification(
      print: (Zone self, ZoneDelegate parent, Zone zone, String line) {
        parent.print(zone, line);
      },
    ),
    onError: (Object error, StackTrace stackTrace) {
      print(error);
      print(stackTrace);
    },
  );
}
