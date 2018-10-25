import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class BasePageStatefulWidget extends StatefulWidget {
    final State<StatefulWidget> state;

    const BasePageStatefulWidget(this.state, { Key key }) :super(key: key);

    @override
    State<StatefulWidget> createState() => this.state;
}
