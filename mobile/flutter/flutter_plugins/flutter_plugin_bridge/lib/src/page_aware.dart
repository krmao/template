abstract class PageAware {
  bool _pageDidAppearFlag = false; // 防止容器第一次打开的时候pageDidAppear执行两次

  void pageDidAppear() {
    _pageDidAppearFlag = true;
  }

  void pageDidDisappear() {
    _pageDidAppearFlag = false;
  }

  void safePageDidAppear() {
    if (!_pageDidAppearFlag) {
      pageDidAppear();
    }
  }

  void safePageDidDisappear() {
    if (_pageDidAppearFlag) {
      pageDidDisappear();
    }
  }
}
