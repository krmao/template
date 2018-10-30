import 'package:smart/headers.dart';

class MinePage extends StatefulWidget {
    @override
    createState() => MinePageState();
}

class MinePageState extends DefaultPageState<MinePage> {

    @override
    bool get wantKeepAlive => true;

    @override
    void initState() {
        titleBarWidget = TitleBarWidget(disableBack: true, title: "我的",);
        loadingWidget = LoadingWidget(isShow: false);
        super.initState();
    }

    @override
    Widget buildBody() {
        return getOnTapWidget(Container(color: Colors.pink,), (){
            showSnackBar("haha");
        });
    }

}
