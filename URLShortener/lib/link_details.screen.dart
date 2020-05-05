import 'package:URLShortener/app_theme.dart';
import 'package:URLShortener/model/shortlinklist.dart';
import 'package:URLShortener/utilities/config.dart';
import 'package:flutter/material.dart';
import 'package:charts_flutter/flutter.dart' as charts;
import 'package:shared_preferences/shared_preferences.dart';
import 'package:http/http.dart' as http;

class LinkDetailsScreen extends StatefulWidget {
  final ShortLinkList shortLink;

  LinkDetailsScreen({Key key, @required this.shortLink}) : super(key: key);

  _LinkDetailsScreenState createState() => _LinkDetailsScreenState();
}

class _LinkDetailsScreenState extends State<LinkDetailsScreen> {
  TextEditingController longURLController = new TextEditingController();
  bool _isLoading = false;
  List<charts.Series<Task, String>> _seriesPieData;
  _generateData() {
    var piedata = [
      new Task('Work', 35.8, Color(0xff3366cc)),
      new Task('Eat', 8.3, Color(0xff990099)),
      new Task('Commute', 10.8, Color(0xff109618)),
      new Task('TV', 15.6, Color(0xfffdbe19)),
      new Task('Sleep', 19.2, Color(0xffff9900)),
      new Task('Other', 10.3, Color(0xffdc3912)),
    ];

    _seriesPieData.add(
      charts.Series(
        domainFn: (Task task, _) => task.task,
        measureFn: (Task task, _) => task.taskvalue,
        colorFn: (Task task, _) =>
            charts.ColorUtil.fromDartColor(task.colorval),
        id: 'Air Pollution',
        data: piedata,
        labelAccessorFn: (Task row, _) => '${row.taskvalue}',
      ),
    );
  }

  @override
  void initState() {
    super.initState();
    _seriesPieData = List<charts.Series<Task, String>>();
    _generateData();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      color: AppTheme.nearlyWhite,
      child: SafeArea(
        top: false,
        child: Scaffold(
          backgroundColor: AppTheme.nearlyWhite,
          body: SingleChildScrollView(
            child: SizedBox(
              height: MediaQuery.of(context).size.height,
              child: Column(
                children: <Widget>[
                  Container(
                    padding: const EdgeInsets.only(top: 40),
                    child: Text(
                      'Your Link\n ' + serverURL + "/" + widget.shortLink.shortURL,
                      textAlign: TextAlign.center,
                      style: TextStyle(
                        fontSize: 20,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
                  Container(
                    padding: const EdgeInsets.only(top: 40),
                    child: Text(
                      'Original Link\n ' + widget.shortLink.longURL,
                      textAlign: TextAlign.center,
                      style: TextStyle(
                        fontSize: 20,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
                  Container(
                    padding: const EdgeInsets.only(top: 16),
                    child: Text(
                      'Clicks: ' + widget.shortLink.number.toString(),
                      textAlign: TextAlign.left,
                      style: TextStyle(
                        fontSize: 16,
                      ),
                    ),
                  ),
                  Container(
                    padding: const EdgeInsets.only(top: 16, left: 16),
                    child: const Text(
                      'You can customize your URL. By changing your short URL',
                      textAlign: TextAlign.left,
                      style: TextStyle(
                        fontSize: 16,
                      ),
                    ),
                  ),
                  _buildComposer(),
                  /*Container(
                    padding: const EdgeInsets.only(top: 16),
                    child: const Text(
                      'or by changing your original URL',
                      textAlign: TextAlign.left,
                      style: TextStyle(
                        fontSize: 16,
                      ),
                    ),
                  ),
                  _buildComposerLong(),*/
                  Padding(
                    padding: const EdgeInsets.only(top: 16),
                    child: Center(
                      child: Container(
                        width: 190,
                        height: 50,
                        decoration: BoxDecoration(
                          color: Colors.blue,
                          borderRadius:
                              const BorderRadius.all(Radius.circular(4.0)),
                          boxShadow: <BoxShadow>[
                            BoxShadow(
                                color: Colors.grey.withOpacity(0.6),
                                offset: const Offset(4, 4),
                                blurRadius: 8.0),
                          ],
                        ),
                        child: Material(
                          color: Colors.transparent,
                          child: InkWell(
                            onTap: () {
                              setState(() {
                                _isLoading = true;
                              });
                              editLongURL(longURLController.text);
                            },
                            child: _isLoading ? Center(child: CircularProgressIndicator()) : Center(
                              child: Padding(
                                padding: const EdgeInsets.all(4.0),
                                child: Text(
                                  'Edit URL',
                                  style: TextStyle(
                                    fontWeight: FontWeight.w500,
                                    color: Colors.white,
                                  ),
                                ),
                              ),
                            ),
                          ),
                        ),
                      ),
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(top: 16),
                    child: Center(
                      child: Container(
                        width: 190,
                        height: 50,
                        decoration: BoxDecoration(
                          color: Colors.blue,
                          borderRadius:
                              const BorderRadius.all(Radius.circular(4.0)),
                          boxShadow: <BoxShadow>[
                            BoxShadow(
                                color: Colors.grey.withOpacity(0.6),
                                offset: const Offset(4, 4),
                                blurRadius: 8.0),
                          ],
                        ),
                        child: Material(
                          color: Colors.transparent,
                          child: InkWell(
                            onTap: () {
                              setState(() {
                                _isLoading = true;
                              });
                              deleteURL();
                            },
                            child: _isLoading ? Center(child: CircularProgressIndicator()) : Center(
                              child: Padding(
                                padding: const EdgeInsets.all(4.0),
                                child: Text(
                                  'Delete URL',
                                  style: TextStyle(
                                    fontWeight: FontWeight.w500,
                                    color: Colors.white,
                                  ),
                                ),
                              ),
                            ),
                          ),
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }

  editLongURL(String longURL) async{

    var jsonData = null;
    SharedPreferences sharedPreferences = await SharedPreferences.getInstance(); 
    var response = await http.patch(serverURL + "/update/LongURL?shortURL=" + widget.shortLink.shortURL 
    + "&longURL=" + longURL,
     headers: { 
       'Content-type': "application/json",
       'Authorization': sharedPreferences.getString("token")});
    if(response.statusCode == 200){
      setState(() {
        _isLoading = false;
        Navigator.of(context).pushReplacementNamed('/home');
      });
   
    }else {
      setState(() {
        _isLoading = false;
      });
      print(response.body);
      print(sharedPreferences.getString("username"));
      print(sharedPreferences.getString("token"));
    }
  }

  deleteURL() async{

    var jsonData = null;
    SharedPreferences sharedPreferences = await SharedPreferences.getInstance(); 
    var response = await http.delete(serverURL + "/deletion/URL?shortURL=" + widget.shortLink.shortURL,
     headers: { 
       'Content-type': "application/json",
       'Authorization': sharedPreferences.getString("token")});
    if(response.statusCode == 200){
      setState(() {
        _isLoading = false;
        Navigator.of(context).pushReplacementNamed('/home');
      });
   
    }else {
      setState(() {
        _isLoading = false;
      });
      print(response.body);
      print(sharedPreferences.getString("username"));
      print(sharedPreferences.getString("token"));
    }
  }

  Widget _buildComposer() {
    return Padding(
      padding: const EdgeInsets.only(top: 16, left: 32, right: 32),
      child: Container(
        decoration: BoxDecoration(
          color: AppTheme.white,
          borderRadius: BorderRadius.circular(8),
          boxShadow: <BoxShadow>[
            BoxShadow(
                color: Colors.grey.withOpacity(0.8),
                offset: const Offset(4, 4),
                blurRadius: 8),
          ],
        ),
        child: ClipRRect(
          borderRadius: BorderRadius.circular(25),
          child: Container(
            padding: const EdgeInsets.all(4.0),
            constraints: const BoxConstraints(minHeight: 80, maxHeight: 160),
            color: AppTheme.white,
            child: SingleChildScrollView(
              padding:
                  const EdgeInsets.only(left: 10, right: 10, top: 0, bottom: 0),
              child: TextField(
                controller: longURLController,
                maxLines: null,
                onChanged: (String txt) {},
                style: TextStyle(
                  fontFamily: AppTheme.fontName,
                  fontSize: 16,
                  color: AppTheme.dark_grey,
                ),
                cursorColor: Colors.blue,
                decoration: InputDecoration(
                    border: InputBorder.none, hintText: 'Change you short URL'),
              ),
            ),
          ),
        ),
      ),
    );
  }
}

  Widget _buildComposerLong() {
    return Padding(
      padding: const EdgeInsets.only(top: 16, left: 32, right: 32),
      child: Container(
        decoration: BoxDecoration(
          color: AppTheme.white,
          borderRadius: BorderRadius.circular(8),
          boxShadow: <BoxShadow>[
            BoxShadow(
                color: Colors.grey.withOpacity(0.8),
                offset: const Offset(4, 4),
                blurRadius: 8),
          ],
        ),
        child: ClipRRect(
          borderRadius: BorderRadius.circular(25),
          child: Container(
            padding: const EdgeInsets.all(4.0),
            constraints: const BoxConstraints(minHeight: 80, maxHeight: 160),
            color: AppTheme.white,
            child: SingleChildScrollView(
              padding:
                  const EdgeInsets.only(left: 10, right: 10, top: 0, bottom: 0),
              child: TextField(
                maxLines: null,
                onChanged: (String txt) {},
                style: TextStyle(
                  fontFamily: AppTheme.fontName,
                  fontSize: 16,
                  color: AppTheme.dark_grey,
                ),
                cursorColor: Colors.blue,
                decoration: InputDecoration(
                    border: InputBorder.none, hintText: 'Change you original URL'),
              ),
            ),
          ),
        ),
      ),
    );
}

class Task {
  String task;
  double taskvalue;
  Color colorval;

  Task(this.task, this.taskvalue, this.colorval);
}
