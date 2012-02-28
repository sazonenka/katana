<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <title>Katana Config</title>
  <link rel="shortcut icon" href="<spring:url value='/resources/images/favicon.ico'/>" />

  <link rel="stylesheet" type="text/css" href="<spring:url value='/resources/css/katana.css'/>" />
  <link rel="stylesheet" type="text/css" href="<spring:url value='/resources/css/gxt-all.css'/>" />

  <script type="text/javascript" src="<spring:url value='/config/config.nocache.js'/>"></script>
  <script type="text/javascript">
    var configDictionary = {"configId": ${configId}};
  </script>
</head>
<body>
  <div id="header">
    <div class="logoslogan">
      <img src="<spring:url value='/resources/images/katana-label.png'/>" style="padding: 10px 0 0 20px;" />
      <img src="<spring:url value='/resources/images/katana-logo.png'/>" style="padding-left: 20px; height: 87px" />
    </div>
  </div>
  <div id="body">
    <div id="container" class="container"></div>
  </div>
  <div id="footer">
    Katana © 2011-2012
    <br />
    Design &amp; Programming: <b>Aliaksandr Sazonenka</b>  
  </div>
</body>
</html>
