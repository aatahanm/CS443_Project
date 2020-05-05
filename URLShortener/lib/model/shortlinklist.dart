
class ShortLinkList {
  ShortLinkList({
    this.userName = '',
    this.longURL = 'NONE-ORGINAL',
    this.shortURL = 'NONE-SHORT',
    this.number = 5,
    this.counter = 0,
  });

  String userName;
  int number;
  String shortURL;
  String longURL;
  int counter;

  static List<ShortLinkList> shortLinkList = [];
}
