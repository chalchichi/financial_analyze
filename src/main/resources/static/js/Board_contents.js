var table = $('#Board').DataTable();
if(window.location.hostname != "localhost"){
    var preurl='http://oh.nasdaqlongshort.kro.kr'
    var preresourceurl = 'http://ohora.iptime.org:8081'
}else{
    var preurl='http://localhost:8080';
    var preresourceurl = 'http://localhost:8081'
}
table.rows()
    .remove()
    .draw();

$('#Board tbody').on('click', 'tr', function () {
    var data = table.row(this).data();
    var url = preurl+'/chartcommentboard?title='+data[1];
    location.href = url;
});

var url = preurl+'/commentdata'
fetch(url, {
    method: 'POST',
    cache: 'no-cache',
    headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
    },
    body: new URLSearchParams({
        email: ''
    })
})
    .then((response) => response.json())
    .then(datajson=>{
        console.log(datajson[0])
        for (let i = 0; i < datajson.length; i++) {
            table.row.add(
                [
                    datajson[i].company,
                    datajson[i].title,
                    datajson[i].writer,
                    datajson[i].views
                ]
            ).draw(false);

        }

        return datajson;
    })
document.querySelector("#writereplybutton").addEventListener('click',writereply)

function writereply () {
    var rep = $('#replyinput').val();
    var url = window.location;
    fetch(url, {
        method: 'POST',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({
            rep: rep
        })
    })
        .then((response) => response.text())
        .then(datatext=>{
            location.reload();
        })
    console.log(rep)
    console.log(url)
}