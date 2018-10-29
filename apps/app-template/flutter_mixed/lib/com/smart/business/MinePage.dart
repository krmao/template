import 'package:smart/headers.dart';

class MinePage extends StatefulWidget {
    @override
    createState() => MinePageState();
}

class MinePageState extends BasePageState<MinePage> {

    @override
    bool get wantKeepAlive => true;

    @override
    void initState() {
        titleBar = TitleBar(disableBack: true, title: "我的",);
        loading = Loading(isShow: false);
        super.initState();
    }

    @override
    Widget body() {
        return Container(color: Colors.pink,);
    }

}
