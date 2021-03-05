import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import './case/platform_view.dart';
import 'package:lib_flutter_base/lib_flutter_base.dart';

class FlutterRouteWidget extends StatefulWidget {
  FlutterRouteWidget({this.params, this.message, this.uniqueId});

  final Map params;
  final String message;
  final String uniqueId;

  @override
  _FlutterRouteWidgetState createState() => _FlutterRouteWidgetState();
}

class _FlutterRouteWidgetState extends State<FlutterRouteWidget> {
  final TextEditingController _usernameController = TextEditingController();

  @override
  void dispose() {
    Logger.log('uniqueId=${widget.uniqueId}, dispose~');
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    Logger.log(
        '${MediaQuery.of(context).padding.top} uniqueId=${widget.uniqueId}');
    Logger.log(
        '${MediaQuery.of(context).padding.bottom} uniqueId=${widget.uniqueId}');
    Logger.log(
        '${MediaQuery.of(context).size.width} uniqueId=${widget.uniqueId}');
    Logger.log(
        '${MediaQuery.of(context).size.height} uniqueId=${widget.uniqueId}');

    final String message = widget.message;
    return Scaffold(
      appBar: AppBar(
        brightness:Brightness.dark,
        backgroundColor: Colors.black,
        textTheme:new TextTheme(title: TextStyle(color: Colors.black)) ,
        leading: Builder(builder: (BuildContext context) {
          return IconButton(
            icon: const Icon(Icons.arrow_back),
            // 如果有抽屉的话的就打开
            onPressed: () {
              BoostNavigator.of().pop();
            },
            // 显示描述信息
            tooltip: MaterialLocalizations.of(context).openAppDrawerTooltip,
          );
        }),
        title: Text('flutter_boost_example'),
      ),
      body: SingleChildScrollView(
        child: Container(
          height: 1000,
          margin: const EdgeInsets.all(24.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
              Container(
                margin: const EdgeInsets.only(top: 10.0, bottom: 20.0),
                child: Text.rich(TextSpan(text: '', children: <TextSpan>[
                  TextSpan(
                      text: message ??
                          "This is a flutter activity.\nuniqueId:${widget.uniqueId}",
                      style: TextStyle(color: Colors.blue)),
                  TextSpan(
                      text: "\nparams: ${widget?.params}",
                      style: TextStyle(fontStyle: FontStyle.italic)),
                ])),
                alignment: AlignmentDirectional.center,
              ),
              const CupertinoTextField(
                prefix: Icon(
                  CupertinoIcons.person_solid,
                  color: CupertinoColors.lightBackgroundGray,
                  size: 28.0,
                ),
                padding: EdgeInsets.symmetric(horizontal: 6.0, vertical: 12.0),
                clearButtonMode: OverlayVisibilityMode.editing,
                textCapitalization: TextCapitalization.words,
              ),
              new TextField(
                enabled: true,
                autocorrect: true,
                style: const TextStyle(
                    fontSize: 20.0,
                    color: const Color(0xFF222222),
                    fontWeight: FontWeight.w500),
              ),
              new TextField(
                controller: new TextEditingController(),
                focusNode: FocusNode(),
                enabled: true,
                autocorrect: false,
                style: const TextStyle(
                    fontSize: 20.0,
                    color: const Color(0xFF222222),
                    fontWeight: FontWeight.w500),
              ),
              InkWell(
                child: Container(
                    padding: const EdgeInsets.all(8.0),
                    margin: const EdgeInsets.all(8.0),
                    color: Colors.yellow,
                    child: Text(
                      'open native page',
                      style: TextStyle(fontSize: 22.0, color: Colors.black),
                    )),
                onTap: () => BoostNavigator.of().push("native"),
              ),
              InkWell(
                child: Container(
                    padding: const EdgeInsets.all(8.0),
                    margin: const EdgeInsets.all(8.0),
                    color: Colors.yellow,
                    child: Text(
                      'open imagepick demo',
                      style: TextStyle(fontSize: 22.0, color: Colors.black),
                    )),
                onTap: () =>
                    BoostNavigator.of().push("imagepick", withContainer: true),
              ),
              InkWell(
                  child: Container(
                      padding: const EdgeInsets.all(8.0),
                      margin: const EdgeInsets.all(8.0),
                      color: Colors.yellow,
                      child: Text(
                        'open willPop demo',
                        style: TextStyle(fontSize: 22.0, color: Colors.black),
                      )),
                  onTap: () =>
                      BoostNavigator.of().push("willPop", withContainer: true)),
              InkWell(
                  child: Container(
                      padding: const EdgeInsets.all(8.0),
                      margin: const EdgeInsets.all(8.0),
                      color: Colors.yellow,
                      child: Text(
                        'mediaquery demo',
                        style: TextStyle(fontSize: 22.0, color: Colors.black),
                      )),
                  onTap: () => BoostNavigator.of()
                      .push("mediaquery", withContainer: true)),
              InkWell(
                child: Container(
                    padding: const EdgeInsets.all(8.0),
                    margin: const EdgeInsets.all(8.0),
                    color: Colors.yellow,
                    child: Text(
                      'push flutter widget',
                      style: TextStyle(fontSize: 22.0, color: Colors.black),
                    )),
                onTap: () {
                  Navigator.push<dynamic>(context,
                      MaterialPageRoute<dynamic>(builder: (_) => PushWidget()));
                },
              ),
              InkWell(
                  child: Container(
                      padding: const EdgeInsets.all(8.0),
                      margin: const EdgeInsets.all(8.0),
                      color: Colors.yellow,
                      child: Text(
                        'returning data demo',
                        style: TextStyle(fontSize: 22.0, color: Colors.black),
                      )),
                  // onTap: () async {
                  //   final result = await BoostNavigator.of()
                  //       .push("returnData", withContainer: true);
                  //   print('Get result: $result');
                  // }),
                  onTap: () => BoostNavigator.of()
                      .push("returnData", withContainer: true)
                      .then((onValue) => print('Get result: $onValue'))),
            ],
          ),
        ),
      ),
    );
  }
}

class PushWidget extends StatefulWidget {
  @override
  _PushWidgetState createState() => _PushWidgetState();
}

class _PushWidgetState extends State<PushWidget> {
  VoidCallback _backPressedListenerUnsub;

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
  }

  @override
  void didChangeDependencies() {
    // TODO: implement didChangeDependencies
    super.didChangeDependencies();
  }

  @override
  void dispose() {
    super.dispose();
    _backPressedListenerUnsub?.call();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          leading: Builder(builder: (BuildContext context) {
            return IconButton(
              icon: const Icon(Icons.arrow_back),
              // 如果有抽屉的话的就打开
              onPressed: () {
                BoostNavigator.of().pop();
              },
              // 显示描述信息
              tooltip: MaterialLocalizations.of(context).openAppDrawerTooltip,
            );
          }),
          title: Text('flutter_boost_example'),
        ),
        body: Container(
          color: Colors.red,
          width: 100,
          height: 100,
        ));
  }
}
