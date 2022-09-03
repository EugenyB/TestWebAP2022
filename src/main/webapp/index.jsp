<%--
  Created by IntelliJ IDEA.
  User: Eugeny
  Date: 10.05.2015
  Time: 20:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <link rel="stylesheet" type="text/css" href="css/test.css" />
    <title>Testing</title>
  </head>
  <body>
  <h1>Testing for GFL QA automation Java courses</h1>
  <h2>Test contains 15 questions</h2>
  <h3>Duration: 7 minutes 30 seconds</h3>
  <div>
    <form action="teststart" method="POST">
      <table>
        <tr>
          <td><label for="name">Your name</label></td> <td><input id="name" type="text" name="fio" value="" required/></td>
        </tr>
        <tr>
          <td><label for="group">email</label></td> <td> <input id="group" type="text" name="group" value="" /></td>
        </tr>
        <tr>
          <td colspan="2"><button class="accept" type="submit" value="Start" >Start</button></td>
        </tr>
      </table>
    </form>

  </div>
  </body>
</html>
