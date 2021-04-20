<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<link rel="stylesheet" href="style.css">
<body>
<div>
    <form class="choose-form" action="./BoardController" method="get">
        <input name="create" value="create" class="hidden-input"/>
        <div class="field">
            <label for="player1">Player1 name:</label>
            <input class="input" required name="player1" type="text" id="player1"
                   value="Player1"/>
        </div>
        <div class="field">
            <label for="player2">Player1 name:</label>
            <input class="input" required name="player2" type="text" id="player2"
                   value="Player2"/>
        </div>
        <div class="field">
            <label for="width">Width: </label>
            <input class="input" required name="width" type="text" id="width" value="19" />
        </div>
        <div class="field">
            <label for="height">Height: </label>
            <input class="input" required name="height" type="text" id="height" value="19"/>
        </div>
        <div class="field">
            <label for="playTime">Play time in minutes: </label>
            <input class="input" name="playTime" type="text" id="playTime"/>
        </div>
        <div class="field">
            <label for="turnTime">Turn time in minutes: </label>
            <input class="input" name="turnTime" type="text" id="turnTime"/>
        </div>
        <div class="field">
            <label for="againstAI">Against AI: </label>
            <input class="checkbox" name="againstAI" type="checkbox" id="againstAI"/>
        </div>
        <div class="field">
            <button class="button is-primary" id="submit" type="submit" >Submit</button>
        </div>
    </form>
</div>
<div>
    <form action="./LoadController" method="get">
        <input name="load" value="load" class="hidden-input"/>
        <button class="button load-button" id="load" type="submit" >Load Game</button>
    </form>
</div>
<c:if test="${param.error != null}">
    <div class="is-danger tag">
            ${param.error}
    </div>
</c:if>

</body>
</html>
