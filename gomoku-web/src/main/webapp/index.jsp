<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<link rel="stylesheet" href="style.css">
<body>
<div>
    <form action="./BoardController" method="get">
        <input name="create" value="create" class="hidden-input"/>
        <div>
            <label for="player1">Player1 name:</label>
            <input required name="player1" type="text" id="player1"
                   value="Player1"/>
        </div>
        <div>
            <label for="player2">Player1 name:</label>
            <input required name="player2" type="text" id="player2"
                   value="Player2"/>
        </div>
        <div>
            <label for="width">Width: </label>
            <input required name="width" type="text" id="width" value="19" />
        </div>
        <div>
            <label for="height">Height: </label>
            <input required name="height" type="text" id="height" value="19"/>
        </div>
        <div>
            <label for="playTime">Play time in minutes: </label>
            <input name="playTime" type="text" id="playTime"/>
        </div>
        <div>
            <label for="turnTime">Turn time in minutes: </label>
            <input name="playTime" type="text" id="turnTime"/>
        </div>
        <div>
            <label for="againstAI">Against AI: </label>
            <input name="againstAI" type="checkbox" id="againstAI"/>
        </div>
        <button id="submit" type="submit" >Submit</button>
    </form>
</div>
<div>
    <form action="./LoadController" method="get">
        <input name="load" value="load" class="hidden-input"/>
        <button id="load" type="submit" >Load Game</button>
    </form>
</div>
<div>
    ${param.error}
</div>
</body>
</html>
