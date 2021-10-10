const urlService = (function () {
    function buildUrlTr(urlHash) {
        return `
                    <tr>
                        <td><a href="${urlHash.originalUrl}">${urlHash.originalUrl}</a></td>
                        <td><a href="${urlHash.shortenedUrl}">${urlHash.shortenedUrl}</a></td>
                    </tr>
               `;
    }

    const getAllUrls = function() {
        $.get("api/url", {}, function (data) {
            $("#urlsTable tbody").empty();
            for (const urlHash of data) {
                $("#urlsTable tbody:last-child").append(buildUrlTr(urlHash));
            }
        });
    }

    const shortenUrl = function(callback) {
        const originalUrl = $("#url").val();

        $.ajax({
                url: "/api/url",
                type: "POST",
                data: JSON.stringify({
                    url: originalUrl
                }),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: () => {
                    callback();
                }
            }
        );
    }

    return {
        getAllUrls: getAllUrls,
        shortenUrl: shortenUrl
    }
})();
