
class ShortLinkList {
  ShortLinkList({
    this.userName = '',
    this.longURL = 'NONE-ORGINAL',
    this.shortURL = 'NONE-SHORT',
    this.number = 5
  });

  String userName;
  int number;
  String shortURL;
  String longURL;

  static List<ShortLinkList> shortLinkList = [];
}
