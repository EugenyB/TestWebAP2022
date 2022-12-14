<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- 
    Document   : run
    Created on : May 7, 2015, 10:43:25 AM
    Author     : Eugeny
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="css/test.css" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Testing</title>
    </head>
    <body>
        <h1>Question № ${current+1}</h1>
        <p>
            ${q.question}
        </p>
        ${picture}
        <p>
            ${q.addition}
        </p>
        <form action="sendanswer">
            <c:forEach var="v" items="${av.variants}">
                ${v}
            </c:forEach>
            <br/>
            <button class="accept" type="submit" value="Accept" name="ans">Answer</button>
            <button class="next" type="submit" value="Next" name="ans">Skip</button>
            <button class="prev" type="submit" value="Prev" name="ans">Back</button><br/><br/>
            <input type="checkbox" name="finish" value="1" />
            <button class="finish" type="submit" value="Finish" name="ans">Finish</button><br/>
            <p>Для завершення роботи відмітьте прапорець і натисніть <b>Finish</b></p>
            <table><tr>
            <c:forEach var="anw" items="${answers}">
                <c:choose>
                    <c:when test="${anw==-1}">
                        <td class="gray">&nbsp;</td>
                    </c:when>
                    <c:otherwise>
                        <td class="green">&nbsp;</td>
                    </c:otherwise>
                </c:choose>

                
            </c:forEach>
            </tr></table>    
        </form>
    </body>
</html>
