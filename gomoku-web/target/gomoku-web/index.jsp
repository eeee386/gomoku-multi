<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<div class="container">
    <form action="./ChooseController" method="post">
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
            <label for="turnTime">Play time in minutes: </label>
            <input name="playTime" type="text" id="turnTime"/>
        </div>
        <button id="submit" type="submit" class="btn btn-primary">Submit</button>
    </form>
</div>
</body>
</html>
