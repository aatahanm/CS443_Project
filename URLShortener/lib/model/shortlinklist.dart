
class ShortLinkList {
  ShortLinkList({
    this.clickCount = 5,
    this.shortURL = 'NONE-SHORT',
    this.originalURL = 'NONE-ORGINAL',
  });

  int clickCount;
  String imagePath;
  String shortURL;
  String originalURL;

  static List<ShortLinkList> shortLinkList = [
    ShortLinkList(
      clickCount: 3,
    ),
    ShortLinkList(
      clickCount: 5,
    ),
    ShortLinkList(
      clickCount: 7,
    ),
  ];
}
