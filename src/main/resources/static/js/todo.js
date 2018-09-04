// Global definitions.
var table;
var todoList;
var selectedRow;

$(document).ready(function () {
    $(document).ready(function () {

        // DataTables (GET)
        table = $('#todoTable').DataTable({
            ajax: {
                url: window.location + 'api/todos/',
                type: 'GET',
                dataSrc: function (input) {
                    todoList = input;
                    return input;
                }
            },
            columns: [
                {"data": "id"},
                {"data": "content"},
                {
                    "data": "createTime",
                    "render": function (createTime) {
                        return toReadableDate(createTime);
                    }
                },
                {
                    "data": "updateTime",
                    "render": function (updateTime) {
                        return toReadableDate(updateTime);
                    }
                },
                {
                    "data": "complete",
                    "render": function (complete) {
                        return complete ? '완료' : '미완료';
                    }
                }
            ]
        });

        //==============================
        // Events
        //==============================

        // Click DataTable's row
        table.on('click', 'tr', function () {
            initEvents();

            if ($(this).hasClass('selected')) {
                $(this).removeClass('selected');
            }
            else {
                table.$('tr.selected').removeClass('selected');
                $(this).addClass('selected');
                if (table.row('.selected').data() != undefined) {
                    selectedRow = table.row('.selected').data();
                    if (!selectedRow.complete) {
                        $('#btnModify').removeAttr("disabled");
                    }
                }
            }
        });

        // Open modify modal window
        $('#modifyWindow').on('show.bs.modal', function () {
            var modal = $(this);
            modal.find('.modal-title').text('할일 수정하기');
            modal.find('.modal-body #submitId').val(selectedRow.id);
            modal.find('.modal-body #submitContent').val(selectedRow.content);
            modal.find('.modal-body #submitComplete').val('false');
        });

        // Click refresh button
        $("#btnRefresh").on('click', function () {
            initEvents();
            table.ajax.reload();
        });

        // Click modify(submit) button
        $("#modifyForm").submit(function (event) {
            event.preventDefault();

            var id = $("#submitId").val();
            var content = $("#submitContent").val();
            var complete = $("#submitComplete").val() == 'true';

            if (content == "") {
                alert("할일이 입력되지 않았습니다.");
                return;
            }

            if (validate(id, content, complete)) {
                ajaxPut(id, content, complete);
                $('#modifyWindow').modal('toggle');
                initEvents();
            }
        });

        // Click add to-do button
        $("#todoForm").submit(function (event) {
            event.preventDefault();

            var content = $('#todo').val();
            if (content == "") {
                alert("할일이 입력되지 않았습니다.");
                return;
            }

            if (validate(-1, content, false)) {
                ajaxPost(content, false);
                initEvents();
            }

        });

    });
});

//==============================
// Ajax
//==============================

// Create to-do (POST)
function ajaxPost(content, complete) {
    var request = {
        content: content,
        complete: complete
    }

    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        url: window.location + 'api/todos/',
        data: JSON.stringify(request),
        dataType: 'json',
        success: function (result) {
            console.log("success");
            table.ajax.reload();
        },
        error: function (e) {
            console.log("error: " + e);
        }
    });
}

// Modify to-do (PUT)
function ajaxPut(id, content, complete) {
    var request = {
        content: content,
        complete: complete
    }

    $.ajax({
        type: 'PUT',
        contentType: 'application/json',
        url: window.location + 'api/todos/' + id,
        data: JSON.stringify(request),
        dataType: 'json',
        success: function (result) {
            console.log("success");
            table.ajax.reload();
        },
        error: function (e) {
            console.log("error: " + e);
        }
    });
}

//==============================
// Utils
//==============================

// Initialize status.
function initEvents() {
    $('#todo').val("");
    $('#btnModify').attr("disabled", true);
    selectedRow = undefined;
}

// Epoch time to 'YYYY-MM-DD HH:mm:ss'
function toReadableDate(epochTime) {
    var date = new Date(epochTime);
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var day = date.getDate();
    var hours = date.getHours();
    var minutes = date.getMinutes();
    var seconds = date.getSeconds();
    return pad(year, 4) + '-' + pad(month, 2) + '-' + pad(day, 2)
        + ' ' + pad(hours, 2) + ':' + pad(minutes, 2) + ':' + pad(seconds, 2);
}

// Fit digits.
function pad(n, width) {
    n = n + '';
    return n.length >= width ? n : new Array(width - n.length + 1).join('0') + n;
}

// Validate inputs
function validate(id, content, complete) {
    if (content == "") {
        alert("할일이 입력되지 않았습니다.");
        return false;
    }

    var matches = content.match(/@\d+/g);
    if (matches != undefined) {
        for (var i = 0; i < matches.length; i++) {
            var compareId = matches[i].substring(1);
            var findElement = undefined;
            for (var j = 0; j < todoList.length; j++) {
                if (compareId == todoList[j].id) {
                    findElement = todoList[j];
                    break;
                }
            }

            if (findElement == undefined) {
                alert("참조할 할일(" + compareId + ")이 목록에 존재하지 않습니다.");
                return false;
            } else if (findElement.id == id) {
                alert("자기 자신은 참조할 수 없습니다.");
                return false;
            } else if (findElement.complete) {
                alert("참조할 할일(" + findElement.id + ")이 이미 완료처리된 상태입니다.");
                return false;
            }

            // Check cross-reference
            var refMatches = findElement.content.match(/@\d+/g);
            console.log("matches : " + refMatches);
            if (refMatches != undefined) {
                for (var i = 0; i < refMatches.length; i++) {
                    var crossCompareId = refMatches[i].substring(1);
                    console.log("compareId : " + crossCompareId);
                    if (crossCompareId == id) {
                        console.log("cross-ref: " + findElement.id);
                        alert("참조한 할일(" + findElement.id + ")과 상호 참조될 수 없습니다.");
                        return false;
                    }
                }
            }
        }
    }

    if (complete) {
        for (var i = 0; i < todoList.length; i++) {
            var matches = todoList[i].content.match(/@\d+/g);
            if (matches != undefined) {
                for (var j = 0; j < matches.length; j++) {
                    if (matches[j].substring(1) == id && !todoList[i].complete) {
                        alert("완료되지 않은 할일(" + todoList[i].id + ")이 존재합니다.");
                        return false;
                    }
                }
            }
        }
    }

    return true;
}