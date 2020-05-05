import 'package:URLShortener/app_theme.dart';
import 'package:URLShortener/utilities/config.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class LinkCreationScreen extends StatefulWidget {
  @override
  _LinkCreationScreenState createState() => _LinkCreationScreenState();
}

class _LinkCreationScreenState extends State<LinkCreationScreen> {
  bool _isLoading = false;
  TextEditingController longURLController = new TextEditingController();
  TextEditingController customizedURLController = new TextEditingController();

  @override
  void initState() {
    super.initState();
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
                    padding: EdgeInsets.only(
                        top: MediaQuery.of(context).padding.top,
                        left: 16,
                        right: 16),
                    child: Image.asset('assets/images/shorturl.png'),
                  ),
                  Container(
                    padding: const EdgeInsets.only(top: 8),
                    child: Text(
                      'Your Link',
                      style: TextStyle(
                        fontSize: 20,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
                  Container(
                    padding: const EdgeInsets.only(top: 16),
                    child: const Text(
                      'Paste a long URL here to shorten.',
                      textAlign: TextAlign.center,
                      style: TextStyle(
                        fontSize: 16,
                      ),
                    ),
                  ),
                  _buildComposer(),
                  Padding(
                    padding: const EdgeInsets.only(top: 16),
                    child: _isLoading ? Center(child: CircularProgressIndicator()) : Center(
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
                              sendLinkCreationRequest(longURLController.text);
                            },
                            child: Center(
                              child: Padding(
                                padding: const EdgeInsets.all(4.0),
                                child: Text(
                                  'Generate Short URL',
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
                  Container(
                    padding: const EdgeInsets.only(top: 8),
                    child: Text(
                      'OR',
                      style: TextStyle(
                        fontSize: 20,
                        fontWeight: FontWeight.w100,
                      ),
                    ),
                  ),
                                    Container(
                    padding: const EdgeInsets.only(top: 16),
                    child: const Text(
                      'Enter your own customized URL.',
                      textAlign: TextAlign.center,
                      style: TextStyle(
                        fontSize: 16,
                      ),
                    ),
                  ),
                  _buildComposerCustomized(),
                  Padding(
                    padding: const EdgeInsets.only(top: 16),
                    child: _isLoading ? Center(child: CircularProgressIndicator()) : Center(
                      child: Container(
                        width: 270,
                        height: 60,
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
                              sendCustomizedLinkCreationRequest(longURLController.text, customizedURLController.text);
                            },
                            child: Center(
                              child: Padding(
                                padding: const EdgeInsets.all(4.0),
                                child: Text(
                                  'Generate Customized Short URL',
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


  sendCustomizedLinkCreationRequest(String longURL, String customizedURL) async{

    var jsonData = null;
    SharedPreferences sharedPreferences = await SharedPreferences.getInstance(); 
    var response = await http.post(serverURL + "/create/SpecialShortURL?userName=" + sharedPreferences.getString("username")
    + "&longURL=" + longURL + "&shortURL=" + customizedURL,
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

  sendLinkCreationRequest(String longURL) async{

    var jsonData = null;
    SharedPreferences sharedPreferences = await SharedPreferences.getInstance(); 
    var response = await http.post(serverURL + "/create/URL?userName=" + sharedPreferences.getString("username") 
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

  Widget _buildComposerCustomized(){
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
                controller: customizedURLController,
                maxLines: null,
                onChanged: (String txt) {},
                style: TextStyle(
                  fontFamily: AppTheme.fontName,
                  fontSize: 16,
                  color: AppTheme.dark_grey,
                ),
                cursorColor: Colors.blue,
                decoration: InputDecoration(
                    border: InputBorder.none,
                    hintText: 'Enter your URL..'),
              ),
            ),
          ),
        ),
      ),
    );
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
                    border: InputBorder.none,
                    hintText: 'Enter your URL..'),
              ),
            ),
          ),
        ),
      ),
    );
  }
}
