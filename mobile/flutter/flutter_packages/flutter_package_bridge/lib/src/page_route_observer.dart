import 'package:flutter/material.dart';
import 'package:flutter_boost/container/boost_page_route.dart';

import 'page_aware.dart';

/// 保存当前Route与RootRoute之间的对应关系
class RouteHolder {
  final Route route;
  final BoostPageRoute rootRoute;

  RouteHolder(this.route, this.rootRoute);

  @override
  String toString() {
    return 'RouteHolder{route: $route, rootRoute: $rootRoute}';
  }
}

/// PageRouteObserver 有俩个作用：
/// 1、提供 pageDidAppear/pageDidDisAppear 事件。
/// 2、提供 统一的生命周期管理。
///
///
/// 一般情况下，每次openURL打开的Flutter页面会有一个Native容器，
///   一个Native页面对应一个业务页面，同时对应一个BoostPageRoute，这个Route我们称之为 RootRoute。
///
/// 所有的RootRoute都被一个全生命周期级别的，顶级的 Navigator 管理。
///
/// 如果页面内部使用了自己的Navigator 或者 MaterialApp，那么会创建自己的Route。
/// 为了统一管理生命周期，自创建的Navigator或者MaterialApp都应该将PageRouteObserver添加到路由事件监听器中
/// ```
///  MaterialApp(
///         ...
///         navigatorObservers: [PageRouteObserver.singleton],;
///         ...
///         );
/// ```
class PageRouteObserver<R extends Route<dynamic>> extends NavigatorObserver {
  static final PageRouteObserver _instance = PageRouteObserver();

  static PageRouteObserver get singleton => _instance;

  // 存放路由堆栈
  final List<Route> routes = [];
  final List<Route> routesPopup = [];

  // 每个页面对应的监听
  final Map<R, Set<PageAware>> _listeners = <R, Set<PageAware>>{};
  final Map<R, Set<PageAware>> _listenersPopup = <R, Set<PageAware>>{};

  // 保存每个路由对应的 BoostPageRout
  final List<RouteHolder> _rootRoutes = [];

  /// 绑定当前页面与路由栈
  void subscribe(PageAware pageAware, BoostPageRoute rootRoute, R route) {
    assert(pageAware != null);
    assert(route != null);

    print("[PageRouteObserver] subscribe $rootRoute, $route");

    // 绑定RootRoute与当前Route之间的关系
    _rootRoutes.add(RouteHolder(route, route is BoostPageRoute ? route : rootRoute));

    // 触发对应PageAware的appear事件
    if (route is PageRoute) {
      final Set<PageAware> subscribers = _listeners.putIfAbsent(route, () => Set<PageAware>());
      if (subscribers.add(pageAware)) {
        pageAware.safePageDidAppear();
      }
    } else if (route is PopupRoute) {
      final Set<PageAware> subscribers = _listenersPopup.putIfAbsent(route, () => Set<PageAware>());
      if (subscribers.add(pageAware)) {
        pageAware.safePageDidAppear();
      }
    }
  }

  /// 解绑当前页面与路由栈
  void unsubscribe(PageAware pageAware, Route route) {
    assert(pageAware != null);

    print("[PageRouteObserver] unsubscribe $route");

    _rootRoutes.removeWhere((element) => element.route == route);

    List<R> needRemovedRouts = List();
    _listeners.keys.forEach((route) {
      final Set<PageAware> subscribers = _listeners[route];
      subscribers?.remove(pageAware);
      if (subscribers?.isEmpty ?? true) {
        needRemovedRouts.add(route);
      }
    });
    needRemovedRouts.forEach((element) {
      _listeners.remove(element);
    });

    List<R> needRemovedPopRouts = List();
    _listenersPopup.keys.forEach((route) {
      final Set<PageAware> subscribers = _listenersPopup[route];
      subscribers?.remove(pageAware);
      if (subscribers?.isEmpty ?? true) {
        needRemovedPopRouts.add(route);
      }
    });
    needRemovedPopRouts.forEach((element) {
      _listenersPopup.remove(element);
    });
  }

  /// 每当新路由添加进来的时候，触发前一个页面的 disAppear事件。
  @override
  void didPush(Route route, Route previousRoute) {
    super.didPush(route, previousRoute);

    print("[PageRouteObserver] didPush $route, $previousRoute");
    // 之后正常页面（非弹窗）入栈之后，才会触发前一个页面的离开
    if (route is PageRoute) {
      // 触发PageExit事件
      if (routes.length > 0) {
        R previousRoute = routes.last as R;
        final Set<PageAware> previousSubscribers = _listeners[previousRoute];
        if (previousSubscribers != null) {
          for (PageAware pageAware in previousSubscribers) {
            pageAware.safePageDidDisappear();
          }
        }
      }

      // 存储在路由栈上
      routes.add(route);

      // 触发相关PageView事件，在订阅的时候触发
      /*final Set<PageAware> subscribers = _listeners[route];
      if (subscribers != null) {
        for (PageAware pageAware in subscribers) {
          pageAware.pageDidAppear();
        }
      }*/
    } else if (route is PopupRoute) {
      // 存储在路由栈上
      routesPopup.add(route);
    }
  }

  /// 每当回退一个路由的时候:
  /// -- 触发当前页面 disAppear
  /// -- 触发上一个页面的 Appear
  @override
  void didPop(Route route, Route previousRoute) {
    super.didPop(route, previousRoute);

    print("[PageRouteObserver] didPop $route, $previousRoute");
    if (route is PageRoute) {
      // 触发PageExit
      final Set<PageAware> subscribers = _listeners[route];
      if (subscribers != null) {
        for (PageAware pageAware in subscribers) {
          pageAware.safePageDidDisappear();
        }
      }

      // 清除路由栈
      routes.removeLast();

      // 触发PageView
      if (routes.length > 0) {
        R previousRoute = routes.last as R;
        final Set<PageAware> previousSubscribers = _listeners[previousRoute];
        if (previousSubscribers != null) {
          Future.microtask(() {
            for (PageAware pageAware in previousSubscribers) {
              pageAware.safePageDidAppear();
            }
          });
        }
      }
    } else if (route is PopupRoute) {
      // 触发PageExit
      final Set<PageAware> subscribers = _listenersPopup[route];
      if (subscribers != null) {
        for (PageAware pageAware in subscribers) {
          pageAware.safePageDidDisappear();
        }
      }

      // 清除路由栈
      routesPopup.remove(route);
    }
  }

  @override
  void didRemove(Route route, Route previousRoute) {
    super.didRemove(route, previousRoute);

    if (route is PageRoute) {
      routes.remove(route);
    } else if (route is PopupRoute) {
      routesPopup.remove(route);
    }
  }

  @override
  void didReplace({Route newRoute, Route oldRoute}) {
    super.didReplace(newRoute: newRoute, oldRoute: oldRoute);

    if (oldRoute is PageRoute) {
      int index = routes.indexOf(oldRoute);
      assert(index != -1);
      routes.removeAt(index);

      routes.insert(index, newRoute);
    } else if (oldRoute is PopupRoute) {
      int index = routesPopup.indexOf(oldRoute);
      assert(index != -1);
      routesPopup.removeAt(index);

      routesPopup.insert(index, newRoute);
    }
  }

  /// 根据 FlutterBoost 的路由配置信息，找到对应的 BoostPageRoute
  /// 根据 BoostPageRoute 和保存的对应关系，找到对应的容器的栈顶 Route
  Route _findRoute(String pageName, String uniqueId) {
    RouteHolder routeHolder = _rootRoutes.lastWhere((element) {
      print("element.rootRoute = ${element.rootRoute}");
      return element.rootRoute!=null && element.rootRoute.uniqueId == uniqueId && element.rootRoute.pageName == pageName;
    }, orElse: () => null);
    return routeHolder?.route;
  }

  /// 整个容器展示的时候
  /// -- 触发栈顶路由的 Appear
  void pageDidAppear(String pageName, String uniqueId) {
    Route route = _findRoute(pageName, uniqueId);
    if (route == null) {
      return;
    }
    final Set<PageAware> previousSubscribers = _listeners[route];
    if (previousSubscribers != null) {
      for (PageAware pageAware in previousSubscribers) {
        pageAware.safePageDidAppear();
      }
    }
  }

  /// 整个容器消失的时候
  /// -- 触发栈顶路由的 DisAppear
  void pageDidDisappear(String pageName, String uniqueId) {
    Route route = _findRoute(pageName, uniqueId);
    if (route == null) {
      return;
    }
    final Set<PageAware> previousSubscribers = _listeners[route];
    if (previousSubscribers != null) {
      for (PageAware pageAware in previousSubscribers) {
        pageAware.safePageDidDisappear();
      }
    }
  }

  /// 整个容器销毁的时候
  /// -- 删除保存的路由
  void pageDidDestroy(String pageName, String uniqueId) {
    Route route = _findRoute(pageName, uniqueId);
    if (route == null) {
      return;
    }
    routes.remove(route);
  }
}
