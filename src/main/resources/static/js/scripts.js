/*!
    * Start Bootstrap - SB Admin v7.0.5 (https://startbootstrap.com/template/sb-admin)
    * Copyright 2013-2022 Start Bootstrap
    * Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-sb-admin/blob/master/LICENSE)
    */
    // 
// Scripts
// 
if(window.location.hostname != "localhost"){
    var preurl='http://oh.nasdaqlongshort.kro.kr'
    var preresourceurl = 'http://ohora.iptime.org:8081'
}else{
    var preurl='http://localhost:8080';
    var preresourceurl = 'http://localhost:8081'
}

var table = $('#datatablesSimple').DataTable();
table.rows()
    .remove()
    .draw();


$('#summernote').summernote({
    placeholder: 'write comment',
    tabsize: 2,
    height: 300,
    toolbar: [
        ['style', ['style']],
        ['font', ['bold', 'underline', 'clear']],
        ['color', ['color']],
        ['para', ['ul', 'ol', 'paragraph']],
        ['table', ['table']],
        ['insert', ['link']],
        ['view', ['fullscreen', 'codeview', 'help']]
    ]
});


window.addEventListener('DOMContentLoaded', event => {

    // Toggle the side navigation
    const sidebarToggle = document.body.querySelector('#sidebarToggle');
    if (sidebarToggle) {
        // Uncomment Below to persist sidebar toggle between refreshes
        // if (localStorage.getItem('sb|sidebar-toggle') === 'true') {
        //     document.body.classList.toggle('sb-sidenav-toggled');
        // }
        sidebarToggle.addEventListener('click', event => {
            event.preventDefault();
            document.body.classList.toggle('sb-sidenav-toggled');
            localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
        });
    }


});

function sleep(ms) {
    const wakeUpTime = Date.now() + ms;
    while (Date.now() < wakeUpTime) {}
};

function getChart() {
    var url = preurl+'/chartdata'
    var resourceurl = preresourceurl
    var formData = document.getElementById('charted');
    $(".overlay").show();
    var table = $('#datatablesSimple').DataTable();
    table.rows()
        .remove()
        .draw();
    fetch(url, {
        method: 'POST',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({
            start: formData.start.value,
            end: formData.end.value,
            ticker: formData.ticker.value,
            add_days: formData.add_days.value,
            limitcount: formData.limitcount.value,
            chk_info: formData.chk_info.value
        })
    })
        .then((response) => response.json())
        .then(datajson=>{
            for (let i = 0; i < datajson.length; i++) {
                table.row.add(
                    [
                        datajson[i].tday,
                        Math.round(datajson[i].close*10000)/10000,
                        Math.round(datajson[i].high*10000)/10000,
                        Math.round(datajson[i].open*10000)/10000,
                        Math.round(datajson[i].low*10000)/10000,
                        Math.round(datajson[i].volume*10000)/10000
                    ]
                ).draw(false);

            }
            return datajson;
        })
        .then((datajson) => {
                const rand2 = Math.floor(Math.random()*10000);
                const email = document.getElementById("Email").textContent

                var html = '                                        <object data="'+resourceurl+'/myplot_' +email+ '.html?name='+rand2 +'"\n'+
                    '                                                width="100%"\n' +
                    '                                                height="500"\n' +
                    '                                                type="text/html">\n' +
                    '                                        </object>'
                console.log(html)
                $('#Targetplot').html(html)
                $(".overlay").hide();

                var formData = document.getElementById('charted');
                sessionStorage.setItem('start',formData.start.value)
                sessionStorage.setItem('end',formData.end.value)
                sessionStorage.setItem('ticker',formData.ticker.value)
                sessionStorage.setItem('add_days',formData.add_days.value)
                sessionStorage.setItem('limitcount',formData.limitcount.value)
            }
        )
        .catch((error) => {
            $(".overlay").hide();
            $('#Targetplot').html('')
            alert("????????? ??????????????????");
            console.error('fetch??? ????????? ???????????????.', error);
    });


}

function show () {
    var url = preurl+'/activelog'
    console.log("show");
    document.querySelector(".background").className = "background show";
    const email = document.getElementById("Email").textContent

    var table = $('#activelogdatatable').DataTable();
    table.rows()
        .remove()
        .draw();
    fetch(url, {
        method: 'POST',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({
            email: email
        })
    })
        .then((response) => response.json())
        .then(datajson=>{
            console.log(datajson[0])
            for (let i = 0; i < datajson.length; i++) {
                table.row.add(
                    [
                        datajson[i].searchTime,
                        datajson[i].startDate,
                        datajson[i].endDate,
                        datajson[i].ticker.company_NAME,
                        datajson[i].ticker.name,
                        datajson[i].add_days,
                        datajson[i].limitcount

                    ]
                ).draw(false);

            }

            return datajson;
        })

}

function close () {
    console.log("close")
    document.querySelector(".background").className = "background";
}

function searchlog()
{
    var table = $('#activelogdatatable').DataTable();
    const array = table.rows('.selected').data().toArray();
    const start = array[0][1];
    const end = array[0][2];
    const ticker = array[0][4];
    const adddays = array[0][5];
    const limitcount = array[0][6];

    $('#start').val(start);
    $('#end').val(end);
    $('#ticker').val(ticker);
    $('#add_days').val(adddays);
    $('#limitcount').val(limitcount);
    close();
    getChart();
}

function runexample()
{

    const start = '2022-04-01';
    const end = '2022-05-02';
    const ticker = 'AAPL';
    const adddays = 10;
    const limitcount = 7;

    $('#start').val(start);
    $('#end').val(end);
    $('#ticker').val(ticker);
    $('#add_days').val(adddays);
    $('#limitcount').val(limitcount);

    getChart();
}

function getnews()
{
    var formData = document.getElementById('charted');
    console.log(formData.end.value)
    location.href = "/news?start="+formData.start.value+"&end="+formData.end.value;
}

function showfull() {
    location.href = "/main/plot";
}



document.querySelector("#show").addEventListener('click', show);
document.querySelector("#close").addEventListener('click', close);
document.querySelector("#searchlog").addEventListener('click', searchlog);
document.querySelector("#news").addEventListener('click',getnews)
document.querySelector("#examplerun").addEventListener('click',runexample)
document.querySelector("#inforun").addEventListener('click',searchinfo)
document.querySelector("#write").addEventListener('click',writeboard)
document.querySelector("#showfull").addEventListener('click',showfull)
document.querySelector("#regist").addEventListener('click',regist)
document.querySelector("#cancleboard").addEventListener('click',closeboard)


function init()
{
    var table = $('#activelogdatatable').DataTable();

    $('#activelogdatatable tbody').on('click', 'tr', function () {
        if ($(this).hasClass('selected')) {
            $(this).removeClass('selected');
        } else {
            table.$('tr.selected').removeClass('selected');
            $(this).addClass('selected');
        }
    });

    $('#example').DataTable();
    var formData = document.getElementById('charted');
    formData.start.value = sessionStorage.getItem('start')
    formData.end.value = sessionStorage.getItem('end')
    formData.ticker.value = sessionStorage.getItem('ticker')
    formData.add_days.value = sessionStorage.getItem('add_days')
    formData.limitcount.value = sessionStorage.getItem('limitcount')

}

function searchinfo()
{
    var formData = document.getElementById('charted');
    const t = formData.ticker.value;
    if(t=="")
    {
        alert("Select ticker")
    }
    else
    {
        console.log("/stockinfo?ticker=" + t);
        location.href = "/stockinfo?ticker=" + t;
    }
}

function writeboard() {
    document.querySelector(".background2").className = "background2 show";
}

function closeboard () {
    console.log("close")
    document.querySelector(".background2").className = "background2";
}

function regist () {
    var resourceurl = preresourceurl
    var url = preurl+'/comment'
    var markupStr = $('#summernote').summernote('code');
    var title = $('#commenttitle').val()
    const email = document.getElementById("Email").textContent
    var formData = document.getElementById('charted');

    var plotpath = '/myplot_' +email+ '.html';
    fetch(url, {
        method: 'POST',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({
            title : title,
            markupStr: markupStr,
            plotpath : plotpath,
            ticker: formData.ticker.value,
        })
    })
        .then((response) => response.text())
        .then(datatext=>{
            console.log(datatext)
            closeboard()
            alert("save complete")
        })


}
init();

