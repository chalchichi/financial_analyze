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