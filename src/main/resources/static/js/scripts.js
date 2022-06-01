/*!
    * Start Bootstrap - SB Admin v7.0.5 (https://startbootstrap.com/template/sb-admin)
    * Copyright 2013-2022 Start Bootstrap
    * Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-sb-admin/blob/master/LICENSE)
    */
    // 
// Scripts
// 

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
    var formData = document.getElementById('charted');
    $(".overlay").show();
    var table = $('#datatablesSimple').DataTable();
    table.rows()
        .remove()
        .draw();
    fetch('http://localhost:8080/chartdata', {
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
            limitcount: formData.limitcount.value
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

                console.log(email.co)
                var html = '                                        <object data="http://localhost:8081/myplot_' +email+ '.html?name='+rand2 +'"\n'+
                    '                                                width="100%"\n' +
                    '                                                height="500"\n' +
                    '                                                type="text/html">\n' +
                    '                                        </object>'
                console.log(html)
                $('#Targetplot').html(html)
                $(".overlay").hide();
            }
        )
        .catch((error) => {
            $(".overlay").hide();
            $('#Targetplot').html('')
            alert("날짜를 확인해주세요");
            console.error('fetch에 문제가 있었습니다.', error);
    });


}

function init()
{
    $('#example').DataTable();
    var formData = document.getElementById('charted');
    formData.start.value = sessionStorage.getItem('start')

    formData.end.value = sessionStorage.getItem('end')
    formData.ticker.value = sessionStorage.getItem('ticker')
    formData.add_days.value = sessionStorage.getItem('add_days')
    formData.limitcount.value = sessionStorage.getItem('limitcount')
}

init();