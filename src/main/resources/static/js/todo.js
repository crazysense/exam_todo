$(document).ready(function () {
    $(document).ready(function () {
        // Global definitions.
        var todoList;
        var selectedRow;

        //==============================
        // Ajax
        //==============================

        // DataTables (GET)
        var table = $('#todoTable').DataTable({
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
            var complete = $("#submitComplete").val();

            if (content == "") {
                alert("할일이 입력되지 않았습니다.");
                return;
            }

            if (validate(id, content, complete, true)) {
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

            if (validate(-1, content, false, false)) {
                ajaxPost(content, false);
                initEvents();
            }

        });

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
        function validate(id, content, complete, isUpdate) {
            if (content == "") {
                alert("할일이 입력되지 않았습니다.");
                return false;
            }

            var matches = content.match(/@\d+/g);
            if (matches != undefined) {
                var references = todoList.filter(function (element) {
                    for (var i = 0; i < matches.length; i++) {
                        if (matches[i].substring(1) == element.id) {
                            return true;
                        }
                    }
                    return false;
                });

                if (references.length == 0) {
                    alert("참조할 할일이 목록에 존재하지 않습니다.");
                    return false;
                }

                if (id > 0) {
                    if (references.filter(function (reference) {
                        return reference.id == id;
                    }).length > 0) {
                        alert("자기 자신은 참조할 수 없습니다.");
                        return false;
                    }
                }

                if (references.filter(function (reference) {
                    return reference.complete;
                }).length > 0) {
                    alert("참조할 할일이 이미 완료처리된 상태입니다.");
                    return false;
                }
            }

            if (isUpdate) {
                var incompleteReferences = todoList.filter(function (element) {
                    var matches = element.content.match(/@\d+/g);
                    if (matches != undefined) {
                        for (var i = 0; i < matches.length; i++) {
                            if (matches[i].substring(1) == id && !element.complete) {
                                return true;
                            }
                        }
                    }
                    return false;
                });

                if (complete && incompleteReferences.length > 0) {
                    alert("완료되지 않은 할일이 존재합니다.");
                    return false;
                }
            }

            return true;
        }

    });
});