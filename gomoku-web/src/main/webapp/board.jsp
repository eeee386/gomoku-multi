<%--
  Created by IntelliJ IDEA.
  User: attilasedon
  Date: 4/8/21
  Time: 6:42 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Game</title>
</head>
<body>
<jsp:include page="/BoardController"/>
<div><span>Remaining Time: </span><span>?</span></div>
<div><span>Remaining Turn Time: </span><span>?</span></div>
<div>
    <form action="./ChooseController" method="post">
        <button id="finish" type="submit" class="btn btn-primary">Finish game</button>
    </form>
    <form action="./LoadController" method="post">
        <button id="load" type="submit" class="btn btn-primary">Save state</button>
    </form>
</div>
<div>
    ${requestScope.game.player1Name}
</div>
</body>
</html>
