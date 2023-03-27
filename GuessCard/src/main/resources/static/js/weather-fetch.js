// https://openweathermap.org/weather-conditions

const boxImgA = document.getElementById('box-a-img')
const boxImgB = document.getElementById('box-b-img')

const boxTempA = document.getElementById('box-a-temp')
const boxTempB = document.getElementById('box-b-temp')

const KELVIN_ZERO = -273.15;

fetch('http://api.openweathermap.org/data/2.5/weather?q=sofia&appid=8dd1b8c6c70655b59ef4f75b4d9fb753')
    .then(data => data.json())
    .then(info => {
        // console.log(info)

        // Formula Kelvin to Celsius 299K - 273.15 = 25.85°C
        boxTempA.innerText = Math.round(info.main.temp + KELVIN_ZERO);
        boxImgA.src = '/img/weather/' + info.weather[0].icon + '.png'
    })


fetch('http://api.openweathermap.org/data/2.5/weather?q=lasvegas&appid=8dd1b8c6c70655b59ef4f75b4d9fb753')
    .then(data => data.json())
    .then(info => {
        // console.log(info)

        // Formula Kelvin to Celsius 299K - 273.15 = 25.85°C
        boxTempB.innerText = Math.round(info.main.temp + KELVIN_ZERO);
        boxImgB.src = '/img/weather/' + info.weather[0].icon + '.png'
    })