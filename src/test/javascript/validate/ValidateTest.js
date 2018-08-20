describe("Validate When Create Todo", function () {

    it('create success', function () {
        todoList = [];

        var result = validate(1, "Cleaning", false, false);

        expect(result).toBe(true);
    });

    it('create fail by empty content', function () {
        todoList = [];

        var result = validate(1, "", false, false);

        expect(result).toBe(false);
    });

    it('create fail by non exist reference', function () {
        todoList = [];

        var result = validate(1, "Cleaning @100", false, false);

        expect(result).toBe(false);
    });

    it('create fail by reference myself', function () {
        todoList = [];

        var result = validate(1, "Cleaning @1", false, false);

        expect(result).toBe(false);
    });

});

describe("Validate When Modify Todo", function () {

    it('modify success', function () {
        todoList = [{
            "id": 1,
            "content": "Cleaning",
            "createTime": 1534733102684,
            "updateTime": 1534733102684,
            "complete": false
        }];

        var result = validate(1, "Cleaning-Modify", true, true);

        expect(result).toBe(true);
    });

    it('modify fail by empty content', function () {
        todoList = [{
            "id": 1,
            "content": "Cleaning",
            "createTime": 1534733102684,
            "updateTime": 1534733102684,
            "complete": false
        }];

        var result = validate(1, "", false, false);

        expect(result).toBe(false);
    });

    it('modify fail by non exist reference', function () {
        todoList = [{
            "id": 1,
            "content": "Cleaning",
            "createTime": 1534733102684,
            "updateTime": 1534733102684,
            "complete": false
        }];

        var result = validate(1, "Cleaning-Modify @2", false, true);

        expect(result).toBe(false);
    });

    it('modify fail fail by reference myself', function () {
        todoList = [{
            "id": 1,
            "content": "Cleaning",
            "createTime": 1534733102684,
            "updateTime": 1534733102684,
            "complete": false
        }];

        var result = validate(1, "Cleaning-Modify @1", false, true);

        expect(result).toBe(false);
    });

    it('modify fail fail by incomplete reference', function () {
        todoList = [{
            "id": 1,
            "content": "Cleaning",
            "createTime": 1534733102684,
            "updateTime": 1534733102684,
            "complete": false
        }, {
            "id": 2,
            "content": "Washing @1",
            "createTime": 1534733105152,
            "updateTime": 1534733105152,
            "complete": false
        }];

        var result = validate(1, "Cleaning-Modify @1", true, true);

        expect(result).toBe(false);
    });

});