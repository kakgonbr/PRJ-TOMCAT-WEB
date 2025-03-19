/*price range input*/
let rangeInput = document.querySelectorAll('.range-input input');
let rangeText = document.querySelectorAll('.range-text input');
let progress = document.querySelector('.progress');
let priceMax= rangeInput[0].max;
const priceGap = 1000;
        
rangeInput.forEach(input => {
    input.addEventListener('input', (e) => {
        let minVal = parseInt(rangeInput[0].value);
        let maxVal = parseInt(rangeInput[1].value);
        if(maxVal - minVal < priceGap) {
            if(e.target.className === 'range-min'){
                rangeInput[0].value = maxVal - priceGap;
            }else{
                rangeInput[1].value = minVal + priceGap;
            }
        }
        else {
            let positionMin = (minVal / priceMax) * 100;
            let positionMax  = 100 - ((maxVal / priceMax) * 100);
            progress.style.left = positionMin + '%';
            progress.style.right = positionMax + '%';
            rangeText[0].value = minVal;
            rangeText[1].value = maxVal;
        }
    })
});

rangeText.forEach( (input) => {
    input.addEventListener('input', e =>{
        let minVal = parseInt(rangeText[0].value);
        let maxVal = parseInt(rangeText[1].value);
        
        if((maxVal - minVal >= priceGap) && maxVal <= rangeInput[1].max){
            if(e.target.className == 'form-control input-min'){
                rangeInput[0].value = minVal;
                progress.style.left = ((minVal / priceMax) * 100) + "%";
            }else{
                rangeInput[1].value = maxVal;
                progress.style.right = 100 - (maxVal / priceMax) * 100 + "%";
            }
        }
    });
});

/*Sort by*/
const dropdownItems = document.querySelectorAll(".dropdown-item");
const chosenElement = document.querySelector(".sort-menu-chosen");

dropdownItems.forEach(item => {
        item.addEventListener("click", function (event) {
        chosenElement.textContent = this.textContent.trim();
    });
});