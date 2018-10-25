import 'package:smart/headers.dart';

class MinePage extends StatefulWidget {
    @override
    createState() => MinePageState();
}

class MinePageState extends State<MinePage> with AutomaticKeepAliveClientMixin<MinePage> {

    @override
    bool get wantKeepAlive => true;

    @override
    void initState() {
//        titleBar = TitleBar(disableBack: true, title: "我的",);
//        loading = Loading(isShow: false);
        super.initState();
    }

    Widget body() {
        return Container(color: Colors.deepOrangeAccent,);
    }

    @override
    Widget build(BuildContext context) {
        return Container(color: Colors.deepOrangeAccent,);
    }

}
