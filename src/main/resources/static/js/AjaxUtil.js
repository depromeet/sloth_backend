let AjaxUtil = {

    sendPostAjax : function(url, param, callbackFn, errorCallbackFn, async){

        let token = $("meta[name='_csrf']").attr("content");
        let header = $("meta[name='_csrf_header']").attr("content");

        let asyncVal = async != true ? false : true;

        $.ajax({
            url      : url,
            type     : "POST",
            async    : asyncVal,
            contentType : "application/json",
            data     : JSON.stringify(param),
            beforeSend : function(xhr)
            {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
                xhr.setRequestHeader(header, token);
            },
            dataType : "json",
            cache   : false,
            success : function(result, status){

                callbackFn(result, status);
            },
            error : function(jqXhr, status, error){

                if(errorCallbackFn){

                    errorCallbackFn(jqXhr, status, error);

                }else{

                    alert("Ajax Call 오류가 발생했습니다.");
                }
            }
        });
    },

    sendGetAjax : function(url, param, callbackFn, errorCallbackFn, async){

        let asyncVal = (async == false ? false : true);

        $.ajax({
            url      : url,
            type     : "GET",
            async    : asyncVal,
            contentType : "application/json",
            data     : param,
            dataType : "json",
            cache   : false,
            success : function(result, status){
                callbackFn(result, status);
            },
            error : function(jqXhr, status, error){
                if(errorCallbackFn){
                    errorCallbackFn(jqXhr, status, error);
                }else{
                    alert("Ajax Call 오류가 발생했습니다.");
                }
            }
        });
    },

    sendPatchAjax : function(url, param, callbackFn, errorCallbackFn, async){

        let token = $("meta[name='_csrf']").attr("content");
        let header = $("meta[name='_csrf_header']").attr("content");

        let asyncVal = async != true ? false : true;

        $.ajax({
            url      : url,
            type     : "PATCH",
            async    : asyncVal,
            contentType : "application/json",
            data     : JSON.stringify(param),
            beforeSend : function(xhr)
            {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
                if(header != null && header != undefined && token != null && token != undefined){
                    xhr.setRequestHeader(header, token);
                }
            },
            //dataType : "json",
            cache   : false,
            success : function(result, status){

                callbackFn(result, status);
            },
            error : function(jqXhr, status, error){

                if(errorCallbackFn){

                    errorCallbackFn(jqXhr, status, error);

                }else{

                    alert("Ajax Call 오류가 발생했습니다.");
                }
            }
        });
    },

    sendDeleteAjax : function(url, param, callbackFn, errorCallbackFn, async){

        let token = $("meta[name='_csrf']").attr("content");
        let header = $("meta[name='_csrf_header']").attr("content");

        let asyncVal = async != true ? false : true;

        $.ajax({
            url      : url,
            type     : "DELETE",
            async    : asyncVal,
            contentType : "application/json",
            data     : JSON.stringify(param),
            beforeSend : function(xhr)
            {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
                if(header != null && header != undefined && token != null && token != undefined){
                    xhr.setRequestHeader(header, token);
                }
            },
            dataType : "json",
            cache   : false,
            success : function(result, status){

                callbackFn(result, status);
            },
            error : function(jqXhr, status, error){

                if(errorCallbackFn){

                    errorCallbackFn(jqXhr, status, error);

                }else{

                    alert("Ajax Call 오류가 발생했습니다.");
                }
            }
        });
    },

}