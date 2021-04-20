<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Load</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<form action="./ChooseController" method="get">
    <input class="button" name="goback" value="Go Back" type="submit"/>
</form>
<jsp:include page="/LoadController"/>
<table class="table">
    <thead>
    <tr>
        <th>ID</th>
        <th>Player 1</th>
        <th>Player 2</th>
        <th>Active Player</th>
        <th>Against AI</th>
        <th>Remaining Time</th>
        <th>Turn Time</th>
        <th>Remaining Turn Time</th>
        <th>Load Game</th>
        <th>Delete Game</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="game" items="${requestScope.games}">
        <form action="./BoardController" method="get" class="load-form">
            <tr>
                <input name="boardState" value="${game.boardState}" class="hidden-input"/>
                <td>
                    <span>${game.id}</span>
                    <input name="id" value="${game.id}" class="hidden-input">
                </td>
                <td>
                    <span>${game.player1Name}</span>
                    <input name="player1" value="${game.player1Name}" class="hidden-input"/>
                </td>
                <td>
                    <span>${game.player2Name}</span>
                    <input name="player2" value="${game.player2Name}" class="hidden-input"/>
                </td>
                <td>
                    <span>${game.isPlayer1Active ? game.player1Name : game.player2Name}</span>
                    <input name="isPlayer1Active" value="${game.isPlayer1Active}" class="hidden-input"/>
                </td>
                <td>
                    <span>${game.isPlayer2AI ? "Yes" : "No"}</span>
                    <input name="againstAI" value="${game.isPlayer2AI}" class="hidden-input"/>
                </td>
                <td>
                    <span>${game.remainingTime}</span>
                    <input name="remainingTime" value="${game.remainingTime}" class="hidden-input"/>
                </td>
                <td>
                    <span>${game.turnTime}</span>
                    <input name="turnTime" value="${game.turnTime}" class="hidden-input"/>
                </td>
                <td>
                    <span>${game.remainingTurnTime}</span>
                    <input name="remainingTurnTime" value="${game.remainingTurnTime}" class="hidden-input"/>
                </td>
                <td>
                    <input class="button is-primary" type="submit" name="load" value="Load"/>
                </td>
                <td>
                    <input class="button is-danger" type="submit" name="delete" value="Delete"/>
                </td>
            </tr>
        </form>
    </c:forEach>
    </tbody>
</table>
<c:if test="${param.error != null}">
    <div class="is-danger tag">
            ${param.error}
    </div>
</c:if>
</body>
</html>
