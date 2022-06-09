if(window.location.hostname != "localhost"){
    var preurl='http://oh.nasdaqlongshort.kro.kr'
    var preresourceurl = 'http://ohora.iptime.org:8081'
}else{
    var preurl='http://localhost:8080';
    var preresourceurl = 'http://localhost:8081'
}

function createaccount () {
    var url = preurl+'/account'

    var formData = document.getElementById('register');
    fetch(url, {
        method: 'POST',
        cache: 'no-cache',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({
            name: formData.inputFirstName.value + formData.inputLastName.value,
            email: formData.inputEmail.value,
            picture: preresourceurl+'/suicide.png',
            password : formData.inputPassword.value
        })
    })
        .then((response) => {
            if (!response.ok) {
                throw response;
            }
            location.href = "/";
        })
        .catch((error) => {
            error.text().then(msg => alert(msg))
        });

}