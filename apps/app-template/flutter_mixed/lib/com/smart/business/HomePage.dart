import 'package:smart/headers.dart';

class HomePage extends StatefulWidget {
    @override
    createState() => HomePageState();
}

class HomePageState extends BasePageState<HomePage> {

    @override
    void initState() {
        titleBar = TitleBar(disableBack: true, title: "首页",);
        loading = Loading(isShow: false);
        super.initState();
    }

    @override
    Widget body() {
        return Container(color: Colors.blueGrey,);
    }

    @override
    bool get wantKeepAlive {
        return true;
    }

}
