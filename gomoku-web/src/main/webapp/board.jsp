<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="style.css">
    <title>Game</title>
</head>
<body>
<jsp:include page="/BoardController"/>
<div>
    <span>${requestScope.hasSomebodyWon ? "Winner: " : "Active Player: "} </span><span>${requestScope.game.isPlayer1Active ? requestScope.game.player1Name : requestScope.game.player2Name}</span>
</div>
<div><span>Remaining Time: </span><span>?</span></div>
<div><span>Remaining Turn Time: </span><span>?</span></div>
<div>
    <form action="./BoardController" method="post">
        <input id="finish" name="finish" value="Finish game" type="submit"/>
    </form>
    <form action="./BoardController" method="post">
        <input id="save" value="Save state" type="submit" name="save"/>
    </form>
    <form action="./ChooseController" method="get">
        <input id="goback" name="goback" value="Go Back" type="submit"/>
    </form>
</div>
<div>
    ${requestScope.error}
</div>
<div class="board-height-wrapper">
    <c:forEach var="h" begin="0" end="${requestScope.height-1}">
        <div class="board-width-wrapper">
            <c:forEach var="w" begin="0" end="${requestScope.width-1}">
                <form action="./BoardController" method="post" class="board-form">
                    <input value="${h}:${w}" name="${h}:${w}" class="hidden-input"/>
                    <c:choose>
                    <c:when test="${requestScope.hasSomebodyWon}">
                    <button type="submit" id="${h}:${w}" class="cell-button" disabled></c:when>
                        <c:otherwise>
                        <button type="submit" id="${h}:${w}" class="cell-button"></c:otherwise>
                            </c:choose>
                                ${requestScope.boardState[h][w]}
                        </button>
                </form>
            </c:forEach>
        </div>
    </c:forEach>
</div>
</body>
</html>
