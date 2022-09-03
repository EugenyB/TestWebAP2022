<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <jsp:useBean id="res" scope="request" type="ua.mk.berkut.server.Result"/>
        <link rel="stylesheet" type="text/css" href="css/test.css" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="https://www.google.com/jsapi"></script>
        <script type="text/javascript">
            google.load("visualization", "1", {packages:["corechart"]});
            google.setOnLoadCallback(drawChart);
            function drawChart() {
                var data = google.visualization.arrayToDataTable([
                    ['Answer', 'Quantity'],
                    ['Correct', ${res.correct}],
                    ['Wrong',    ${res.wrong}]
                ]);

                var options = {
                    title: 'Test result',
                    is3D: true,
                    backgroundColor: '#ffffe0',
                };

                var chart = new google.visualization.PieChart(document.getElementById('piechart_3d'));
                chart.draw(data, options);
            }
        </script>
        <title>Testing - Result</title>
    </head>
    <body>
        <h1>Result of testing</h1>
        <h2>${res.fio} - ${res.group}</h2>
        <div id="piechart_3d" style="width: 900px; height: 500px;"></div>
        <h3>Correct: ${res.correct} з ${res.total}</h3>
<%--        <table style="background-color: black">--%>
<%--            <thead>--%>
<%--                <tr>--%>
<%--                    <th style="background-color: beige">№</th>--%>
<%--                    <th style="background-color: beige">Answer</th>--%>
<%--                    <th style="background-color: beige">Correct</th>--%>
<%--                </tr>--%>
<%--            </thead>--%>
<%--            <tbody>--%>
<%--            <c:forEach var="r" items="${res.result}">--%>
<%--                <c:choose>--%>
<%--                    <c:when test="${r.yours==r.correct}">--%>
<%--                        <tr class="correct">--%>
<%--                    </c:when>--%>
<%--                    <c:otherwise>--%>
<%--                        <tr class="wrong">--%>
<%--                    </c:otherwise>--%>
<%--                </c:choose>--%>
<%--                    <td>${r.n+1}</td>--%>
<%--                    <td>${r.yours+1}</td>--%>
<%--                    <td>${r.correct+1}</td>--%>
<%--                </tr>                --%>
<%--            </c:forEach>--%>
<%--            </tbody>--%>
<%--        </table>--%>
        
    </body>
</html>
