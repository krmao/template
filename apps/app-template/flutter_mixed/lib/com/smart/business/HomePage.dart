import 'package:smart/headers.dart';

class HomePage extends StatefulWidget {
    @override
    createState() => HomePageState();
}

class HomePageState extends BasePageState<HomePage> {

    @override
    bool get wantKeepAlive => true;

    @override
    void initState() {
        titleBarWidget = TitleBarWidget(disableBack: true, title: "首页",);
        loadingWidget = LoadingWidget(isShow: false);
        super.initState();
    }

    @override
    Widget body() {
        return getOnTapWidget(Container(color: Colors.blueAccent), () {
            showSnackBar("you clicked");
        });
    }

}
