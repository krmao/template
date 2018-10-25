class LoginOrLogoutEvent {
  bool isLogin;

  LoginOrLogoutEvent(bool isLogin) {
    this.isLogin = isLogin;
  }

  @override
  String toString() {
    return 'LoginOrLogoutEvent{isLogin: $isLogin}';
  }
}
