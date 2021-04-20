<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<jsp:include page="/BoardController"/>
<html>
<head>
    <link rel="stylesheet" href="style.css">
    <title>Game</title>
    <% Integer playTimeStr = (Integer) request.getAttribute("playTime"); %>
    <% Integer turnTimeStr = (Integer) request.getAttribute("turnTime"); %>
    <!-- This is here because otherwise it will cause a ERR_INCOMPLETE_CHUNKED_ENCODING error -->
    <script type="text/javascript" defer="defer">
        function onLoad() {
            let playTimeId = "playTime";
            let turnTimeId = "turnTime";
            let playTimeInputId = "playTimeInput";
            let turnTimeInputId = "turnTimeInput";

            function setTimeInputs(inputID, value) {
                const inputs = document.getElementsByClassName(inputID);
                for (let i = 0; i < inputs.length; i++) {
                    inputs[i].setAttribute("value", value);
                }
            }

            function formatTime(time) {
                return (Math.floor((time / 60))) + ":" + ((time % 60) < 10 ? "0" : "") + (time % 60);
            }

            function timeHandler(value, id, endHandler, setInput) {
                const element = document.getElementById(id);
                element.value = formatTime(value);
                setInput(value);
                if (value === 0) {
                    return endHandler();
                }
                value--;
                setTimeout(() => timeHandler(value, id, endHandler, setInput), 1000);
            }


            <c:if test="${requestScope.turnTime != null}" >
            <c:out value="${requestScope.playTime}"/>
            <% int turnTime = turnTimeStr; %>
            let remainingTurnTime =<%= turnTime%>;
            console.log(remainingTurnTime);
            const endTurnHandler = () => window.location.replace("board.jsp?isTurnFinished=true");
            const setTurnInput = (value) => setTimeInputs(turnTimeInputId, value);
            timeHandler(remainingTurnTime, turnTimeId, endTurnHandler, setTurnInput);
            </c:if>

            <c:if test="${requestScope.playTime != null}">
            <% int playTime = playTimeStr; %>
            let remainingPlayTime =<%= playTime %>;
            const endTimeHandler = () => window.location.replace("board.jsp?isPlayTimeFinished=true");
            const setPlayInput = (value) => setTimeInputs(playTimeInputId, value);
            timeHandler(remainingPlayTime, playTimeId, endTimeHandler, setPlayInput);
            </c:if>
        }

    </script>
</head>
<body onload="onLoad()">
<div>
    <span>${requestScope.activeWinner} </span><span>${requestScope.playerNameToShow}</span>
</div>
<div>
    <span>Remaining Time: </span>
    <span>
        <form><input value="" type="text" id="playTime"/></form>
    </span>
</div>
<div><span>Remaining Turn Time: </span>
    <span>
        <form><input value="" type="text" id="turnTime"/></form>
    </span>
</div>
<div>
    <form action="./BoardController" method="post">
        <input value="" name="turnTimeInput" class="hidden-input turnTimeInput"/>
        <input value="" name="playTimeInput" class="hidden-input playTimeInput"/>
        <input id="finish" name="finish" value="Finish game" type="submit"/>
    </form>
    <form action="./BoardController" method="post">
        <input value="" name="turnTimeInput" class="hidden-input turnTimeInput"/>
        <input value="" name="playTimeInput" class="hidden-input playTimeInput"/>
        <input id="save" value="Save state" type="submit" name="save"/>
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
                    <input value="" name="turnTimeInput" class="hidden-input turnTimeInput"/>
                    <input value="" name="playTimeInput" class="hidden-input playTimeInput"/>
                    <c:choose>
                    <c:when test="${requestScope.hasGameEnded}">
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
