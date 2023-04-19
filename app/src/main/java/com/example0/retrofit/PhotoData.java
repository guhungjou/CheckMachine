package com.example0.retrofit;

import android.util.Log;

public class PhotoData {
    private static PhotoData photoData;

    public  String  image;
    public String test;
    public float[][] bounding;

//    public static PhotoData getInstance(String image) {
//        if (photoData == null) {
//            photoData = new PhotoData(image);
//
//        }
//        return  photoData;
//    }

    public PhotoData(String image,float[][] bounding) {
        this.image = image;
        this.bounding=bounding;
    }
    //    public static PhotoData getInstance(String test) {
//        if (photoData == null) {
//            photoData = new PhotoData(test);
//
//        }
//        return  photoData;
//    }
//    public PhotoData(String test) {
//        this.test = test;
//    }
      public  PhotoData (){
        this.image="/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAHgAoADASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD5/ooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAorZ/sW24xfsf+2H/wBel/sW1yP+Jg3v+4/+ypXQWMWitg6Paj/mIE844h/+vQdHtNwUag5J/wCmH/2VO4GPRW7HoVo5wdScHP8Az7//AGVaH/CG25iLrqxPHA+zdf8Ax6p50OxyVFbcmh20bFft7kjr+4/+yqP+ybX/AJ/mz6eR/wDZU7oLMyKK1f7LtM/8fsv/AH4/+ypP7MtssPtknHT9x1/8eougsZdFah0y2H/L5J/34/8AsqQabbZ/4+5Mf9cR/wDFUXCxmUVqjSrUjP2yUf8AbAf/ABVH9l2w63cv/fgf/FUcyHysyqK1DpltkYupf+/A/wDiqQ6ZB/z8y/8Afkf/ABVHMhWMyitMaZAf+XmTP/XEf/FUf2ZCOtzJ7fuh/wDFUcy2DlZmUVonTYQcfaX/AO/X/wBekOnxDpcP/wB+/wD69FwsZ9FXzp8f/Pdv+/f/ANek/s9P+ex/74/+vRcRRoq6bFe0p/74/wDr037EP+ev/jv/ANemBUoq39iB6S/+O/8A16X7CP8Anr/47/8AXoAp0Vb+xf8ATT/x2gWWR/rP/HaLgVKKt/Yv+mn/AI7R9i/6af8AjtAFSirf2If89P8Ax2j7F/00/wDHaLgVKKt/Yf8App/47S/YMj/Wf+O//XoAp0Vc+w/9Nf8Ax2j7D/00/wDHf/r0AU6Ku/2eP+ev/jv/ANegaeD/AMtv/Hf/AK9AFKirv9nj/nr/AOO//Xpf7PH/AD2/8d/+vSuBRoq9/Z3/AE1/8d/+vQdO/wCmv/jv/wBemBRoq/8A2b/01/8AHf8A69H9m/8ATb/x3/69K47FCir39njP+t/8d/8Ar0f2eB/y2/8AHf8A69F0FijRVz7Bj/lr/wCO/wD16T7F/wBNP/HaLhYqUVa+xf8ATT/x2l+xf9NP/HaLiKlFaVrpBup0i87bvIGSv/166K+8AfYrOWc6mHMa52+RjP8A49UupFOzKUJNXOLoq82nYOPN/wDHf/r00WGf+Wn/AI7VXJKdFXRp+T/rcf8AAf8A69OGm5P+u/8AHf8A69F0OzKFFX/7N5x5v/jv/wBenf2V/wBNv/HP/r0XQWZnUVpf2V/03/8AHP8A69B0of8APf8A8c/+vRcLMzaK0v7J/wCm3/jn/wBekOl4/wCW3/jv/wBei4rGdRWj/Zf/AE2/8d/+vSDSzn/W/wDjv/16YGfRWgdMA/5b/wDjn/16T+zc/wDLX/x3/wCvSuBQoq8dOx0l/wDHf/r0v9m/9Nf/AB3/AOvTAoUVe/s7/pr/AOO//XpP7P8A+mv/AI7/APXouBSoq9/Z3rL/AOO//XoOnY/5a/8Ajv8A9egCjRV4ad/02/8AHf8A69H9nD/nt/47/wDXoAo0Vf8A7OH/AD2/8d/+vTTp/wD00/8AHf8A69A7FKirv9nH/np/47/9enDTcjPm/wDjv/16BFCirzacR/y0z/wH/wCvQdO/6bf+O/8A16ErhY3DYTc7eg70HT5TxxmukNoAPU96QWpPbFIDnV02TueaDpsgwSwP0FdH9k56dO9KLT3FFgOfhsJNwO7oa6eysy9phiMDgYHNRLbbT05rY06LahGKia0KichqGlFZ2O7gn06VR/sw5+9j8K7TUbUEg4ORWb9mJ64pxV0EvI5s6YeQsnzemKQabzwxrpDafKSMfWgWg5OD9MVRJz39l99x/KlGlZA5Pscda6M2o46D2oFqOgwBRoho5/8AsnkfOfpig6UP7x4roRa9OlBtuM4oA5w6UMn52oGlc8O35V0ZtenrTvsoUUmgOaOlA8b2/AUf2WMj5mNdGLYelH2QEjrn0xTsBzv9koefMfH0oOlJnGWx3ro/spHOMUgtQTuwPwpqwjnP7KQDG4mg6Qnq3510Rs8N046077KSORiiwHNf2RHx8zdfWl/siPHJb6ZrpfsyjPTFILUdDiiwHN/2RHwMsPxp39kRccPz/tV0f2Qc5xn0oFsAehFFgOc/smLjO7060v8AZEXYE/jXRfZRkEDP0oNuo7YHqO9FgOc/siJT0Yn0zTv7Ji7qfzrozbA9uKPs4xj9aAOc/suE4+WlGlRcKI/1rojbDJAXJ7UG2HcYPanYRzx0qAfwfhmlOmRbeF5roBag80fZ1HIH4UWA57+y4gPuD2NOOlw8ny+K3/sw9KBb57UWAwf7NgOMRL/KnLpcHOUzW6LfHBHFO+zkAgD68UDRgf2Zbg8QjmgaZbj/AJZjPfNdCbZeAcY+lAttuOntSsBz/wDZcWf9StB0yHBPlDOenrXQLbL3BoFsC2cDimIwP7NhPSMfgKcNOh/55Acdq3fsgGMcYp32ZTxSaGjn/wCzYT/yyGfXFJ/Z0APMY+hFdF9nXHTkcik+zLnseaVgOcOmQDB8peKYdMh2n90tdEbUYwO/c0w2oHAJPv2ppCZz/wDZsHH7oe1KumRA8Rgc55roBbKcjFC24z0H407AZ+k6ZANRhzGD846iuq1q3VtMmUrkMQDVPS7dRdxH0bjNbWsKPsB6cydaxlH3jaMrRPOm0qHn93yKa2mQggiMEV0DW65OKU26+h/GtrGRzw02Ek5iHHpTl0yHH3MEcZrdW3H+Jp32YbgeopWAw00yB2OE4+tS/wBlRFQNn61ti3API4pfs4HTIpWKZgHTIwACmB9e9H9mREY2c54roPJ46c00Qr6c9qdiTBOlxkkAe2KY2lxA4IJH1roRbjPFL9nUDjmgDnBpkQ4APtmj+y4sdCPxroPIUNkLjFNMAOOMUAc+2mRknAIz70w6an3QpP410BtuuKTyAegHHNAmYH9mRkc5x9aP7LToMj05rfFuCTzz70hgG3JGaYIwf7Ljxyx6dRTW0uLgru6Vv/Zlz0ppt8NTSAwv7MTgnNMbToy2BnFdCYAQQBSG24HfFNRDUwf7MTpk80HS0UHg5+tb32ZepFJ5A9KLIV2YI06MZOTx6Uv9mp2JJ71u+QW7Cj7OMnsfSlYEzD/s1MHlqa2mBT1OK3xbD2FBtgSQDn8Kdijnv7OUsfvUh01Q3OfYmuhNooHAI+tKLcbdrA+1NWJ6ml5X5UnlDr2rXGkz8YT9ad/Y9zj/AFf61kijF8snoKPK9Ritr+xrrnKKP+BUn9jXR6xjHqWFMTMgRc9M1dswVkI4watjRbnGQq49C1TwaRco4O1D/wACpNDTM++iJXn8qzfK9q6u40md4jgLn61QOi3B7Jn/AHqURsxDFxjFJ5Z6mtv+xLn/AGB9TSnRLnr+7/76qrCuYflj0pTFn2rZOiz4J+TP1pf7FnI6pj60WC5jiLgZGaNhAwMVs/2NP/ej/Og6NN6p+dAGKEJ5xigRnPFbP9jzDqyfnR/Y8nHzrmgDG8rGPWjy+enNbB0iQdHT9aP7JkH/AC0XP0p2AyPLyCOlJ5ZHA4+la50mT++v4Ck/suT++v5UrCuZJQdMHNJszWyNJOeZV/I0f2Sf+eo/KnYVzHEY60vlj15rYXSfWUfgKb/ZPYzf+O0DMkJ8o44o8sk4xWuNMwOZcj020DTgDzIfyoAydn4GjbWqdOX/AJ6H/vmk/s9M5MjflRZhcy9tLsAzwK0v7Pj672/KlNgmPvEe9OzFcyxH+lIYs84+tav2KMd2/KkNnGMfM1FmFzLCY+tL5fpzWl9jiznLUv2SId2FFmBm+VnkikEZzWkLSIf3vzp32aLtup2Y9DMEeOnP1p2w7enFaAs4gf4vzo+yR5HX86VmDZnqnfpQVGcmtIWyZ70fZoiO/WiwXM7b/wDXpRH1OK0Ps0QHCn86QW8fXBz9aOViuUdnHQUnl+1aAgj9D9M0C3jB6Z/GjlY7ooeXnik8voP0rQMEeOQfzpPITPQijlYXM4p2pDHgdOlaJgTrg/nSeQh7H86aiwujO8v16+tKI+RV/wCzx98/nSiCMdjinysV0M0yPN/FwcZrT1Yf6GvTlzTNNt4xdpwRznrVzU4VNsgyeprNp81i09DljHjJFJ5ZI6VpfZ0xgg/nSC2QDODWnKyLmeIuOBTljq/9nQdzQLZe2eanlY7oo7ADzTggJ5HWrn2ZeOtOFugxyTSswuUTHzRsyPpV3yEHc0eSvvRZhcomPNMMZ65q+Yhg800xA9zTUWFyjsAGTSFCw+XrV4wqe9N8lRzk0JMGymEpCgq2Yx9KQxg9KOViUkymyHHAppT2q95S9zTTGo+tNJiZT2EHGPzo8rGauNEuaQRqD1o5WHMin5XYA0mzg1cKAdKQxrTsxXRU8vIoWLrlc1aVBn2pfLVe5p2YJoqeXhsgUFAe3NWxEMUeUPelZlXKoQAe9AjI9KteSOoOaBCO9FmJMqFcmjyieSTmrRhweDQYM8kk0nFgmjuQnrS7OafilIzUFEYXjgUuzAqVQAKXaD9aBkIQY/rSquGzUuz8qcqH8KBCsuU4FVNmDWjsygqs6cmkhlbb7UFT3qbbxSbMDmmBAU46U3GKnKmm7aAIipPSmlc1OV4xTNuKAIivrTCvFTkU0pQBABQRg1IRSFeKoRCRTWXipiBjNMK0hNEYGKDT8Uh+lMCOkJOOBTs00nFMBhJ7008dKd14FNPJxTAbzSZp+KjIoEBNIWyMUHpTSM0xCk0mePelxmmn9KBiZBFGeMGjr0puR3poBc4GKP1pvIppkHfinoBKDilDfnWNfa/a2PBZZX/uq44rFk8aXG4+XaQ7e2WNS2kB2W4DigHmuSj8aZX9/ZqD/wBM3/xqdfFlsf8AllJnsKLoLM6cUnWse31+2mAJYq3pxWhFeQyRl0kUgdeRxQmgsT9+acBVZL2FzgMM+gOasKwPT+VVuLYdj1pQBnmjHIGaUjHfmhDGFecUmKfnBzQBmqEMx+dOCkEZFPVakEeRTAmsFJuFAHvU2oZ8uP0NP06L/SAfY9affx/LEPQdaztqVfQxio60hUgcVZMfPSmMgHFaIzuQdeKNp7U4rnvS4x1NJoaGgHPNGBnGKd9KTbjkVNirjD6UnAqQg96acZ5oFYZTRTzznFNUHHNADTmmEetSnjmm43UICMjmjGPenHik6U7CSsMOKTgU5jTMZpoTA8c0w49KfnPSjoKBbjAcnpR0p3AGBTcc02IBS4BFOHFKcDmkVbqMxjvSY4OTTs+1N2nNOwgXhfajluaUAjrxQfrQAh60qn0pccdeKbyDgUrBsd2tPA4pBT15571zG4AevFKq5NOGKeq85xQJgkee1PEJ7CrtrB5jAKMmtuDRN8YZztJp2Fc59YSY+nFVZIsPyK686MyrwQfYVl3WlyIxJjIpFHPlMcYphXFaM1uUPIxVV48dqAKxWmFaslajK0AQleOaZt4qcr6U0rQBBjtSEZqX+dNIoAhK4ppB71KRTfwoAiIph+lSt1phFMTGHkUwjipCMdKYadhEeKQrTyMU00wIyMc00gGnEZ5ppIz05oAjPFIetOP6UzjNMQjdaQkfhQaRjjpTQAenFNJzxS5zzmmkgc0AIeOtRXFylrF5krKi+pOKztZ1iPTk2hgZ2Hyoe31riru7kvJt8jknrk/0pN2Cxu6j4tkDlLKNAv8AfcZJ/CsG51W9ugRPO7ZPPaqxIzxyfWm7cdclu1S3cdhysoHTmhEXOW6U0Yxk/lQCc9DipKHhMyckAZ6mpGQfwsW96i+Ve/NODgnOKTAQEqcg80oduhP4dKft3DpimlAMjsO9K47F7TL0Wt2jO5RScMwGeK7i11ewndIo7oM7nC5UjP515yq4IOQasRsdynJwO2apSFa56hilwD0rmNO8RfNHFNEQg43bufx9a6W3nS5iWRcYbnGa2jJMlqw7GeKeqjGB1oGCakRMmrJHJGSQKsxwZx6062hLHAFdNpehPdYZV+Ud6GG5m6XYl7kcYGDyam1TT3WKE4H3efau3sdFgszuPztjHtUl9pkN3FhVCuowpqOZXK6HlUkO3qOPpVOVMHNdVqunG0dlb72f0rnp1AJqySht5pCM1Iw55phGe1JghuBSYOadSGkMaRimH1Oaee1JTBDCKQnNOOB2pCDjNAMZikzxS84oGCKLCTGnjtUZzk1IcDmmd80AxMe9Mb86dnB5pME00SxvbpRzQOtPweKGAwLyDnilIAp2AOaaRzmkAn0o5Ipenal7ZFMSGAmlwfwpSOfSl7UDsN2565pDmpO1NIY+9A2hoHNKAM560o4PNHrkcUC22O5FSDB7VGtSD1rlZsSKAanRRVdTz7VOhpgalgyxTox6A9q65CGUMOhFcPDJt9sVu6dqewCOTlO3tVXuibWZu0hAYYIyKRJFkQMhyDTqko53VrdY5CFXA61hSriui1SVZHOw8CsKUdaAKTJ7VGVqwy0wigCsVxTStTlRTCKAICtNPtUzCmEe1AEBHU03AqU9cUwrTAjI9qYR2qUgVGaAIyOaYRmpCOKYfagCPHrTW/SnkGmEYpiIjzmmHg81Iw5qNh1piGnGDTOKcaYc0xMQ+lNbrxQ3Jpp+X/GmAECsPXNcFihitiDP3J5C/wD16Nb1hLIGGNi05HGOi+5rhrq7eaQlmLE9Se9JsYs9w9xMZZW3MTkknrTGcvgg4Wq+7JyTRvIOc/hUXGWAABupock8fhUKsztj86lLqo4pALlVPzElutJvLE9aaTnk0ikmgY5eeSaeGwc9TTABjJpQBwc/WkBPGxY/MeP5VMY92Cpz9KqhizcDip95ZhzwOgosNEgiOB60qpknBBxTN7OBnpUuyQqWPT2qRj43MTBlPIrW0/U2tpAxXqcnnpWOsW0HPX160o4x+tNNoNz0ezvIbyLdG4J7gdq0Ihg815pZ3k9rIrRsVYd+xrvdN1KK9hDKpQ9wea6ISuZyjY6bTk3SDpivSdKREsE2Y564rzCzm2uBmu80TVIREInb5jjNE9ho6DFFAIYAg5B6GmTTRwRl5GAA9e9ZBY43xM4N5JyDiuPuByTW7rl55ty555Nc5M+7vW6RLK7jn2pKDmmk0MEKwpuM9qWmmgYhFN/GndOaaSTyKBDQKDntSk000WEJ3puOacaQjqaYhhAPWkIGM07+dKB70MLEWOaQjrUpXNNx68UAkR4HWlBwM0EYNA5NG4rAevsaaOtHc8UfWkOwpOOuKcuDTcd6UD3oYLcccdqQrzmijNAxPoOKTHftTgPSj6U0TqIBig89KdtyKTAzxQM7INgVIG4qqHwKesnauU1LYbmplOeRVVW4qVX7UwLSuQOKtRyY71QVqmR8c0kBuWV88R4YnPbNWbjUpGTHQe1YUT8jB5qZ5CV60wHSylznt6VXc7j0pxNMLUARMBUbCpWPGajbrxQBGRUZXvUrVG1AETCmN0p7H0phx70ARmmGnk+9Rs1ADDTG9qexqNjQAwmmGnE0wnmgBrUwnink+vSomqkIYTzUbGnnGOvNRMSBVCA1GSKUtgdaiZqAYE1lazq8emW45DTv9xf6mr086W8LzSttRF3MfavNNU1KbUL6S4duD9xf7q9hSbGiO4unndndizNySe5qoT9PekJYZpp9Ki4BkkYpRzgUg/KnqvPAzmkMUNtBANAz1By3ajyjuyc04I2c4oQChSTwOaUIR3+oqQHinCNpOhwadgIW4Pt7UoVj0FXIdPkeQZGBjOa047OK3iysJZj0J6VSg2K5jrG4XAWrKWkpVWKsM9OOtaKRiPloju7cdTUM927vgBt44yT/ACquSy1C5B5YiA3EZ9KcJ8cY/CiO1lmOSM5Pr0rRtdFmkAIAweM+lZOLLuiix3LwMGogHB5HJrdu9KaCLg5wOay3QIjHafbNOUbLUVymGbzSQcY65rtvBt5Awe0mCea+Sjt/KuFZgCcVZs53R1KMQwbIYdqINpja0PXInIOOcitO2uzDg5rnNN1Ualarcf8ALUACRR2Pr+NXPP3HjNdC1MjuNN8QTKrKSWG04HoarazrUksoGcfKKwNOuMGUk/w1FqVxmYY6bRStqO+gl1dGTJNZsj88UNMTnmoGcGtEZskzSHIGKYHFLvqSh3INJ703dmgtjtSKDvSEc+1IWxTS/vQIcSBTS2aYXppJoEPz60meetMJpN1O4D6AcGmbs8UgPNIGTAk5pp60wE807PFADDTc8+9OJ5puMGgLC54560maQjBzmk75zQw1JBjHJpM4FMyaXdxmgB4bg0H0pq5HTmndvSgBp9qeo4pO+M0dOKBWH8DvSH16UhPNAbccGkM6MSDHNPWTnrWb57H1NPEx4Nc1zQ0xN71KswrIFxinrdE8c1QGyswFTLOMYrEF0fTipFuznoaAN6KbDA5qy0o29ea56O7bcODVprxtmcE0gNPzR60eYPWscXx9D+VO+2nHQ0AapcVGX4xWb9tOOhphvT6GgZpFwaiaQCs83nbBppu89jTEXS9Rs4I4qmbo56GmG5FAFpnxTSwNVDcD3zSG4zQBYZ6Yz1XabPY00zcUATs9NLelQecBTfO9jQBMzZ4zUZNRmQU0yCmA5s+lQs2AOKd5mTzUbHtVITGFsVEzjNVLzUrW1lMU06q4528k1UOs2J/5eR9dpqkhGX4v1LbCljGcF/nk+nYVxjHJ+laWt3X2rUZpgcrkKp9hWYPWs2NA5zikA54px5B6U6FCzYHXoBSSvoMktbV7qdYkGSa1l07YNi9O5I5rZ0uwFjZhioEzj5j6e1SRxJJKAMEZrojQ6syczHXSZHYAJxjgipv7Am2BtjHHXA7V0n2ux01c3Djd1CLyahm8bW6jYlkAPVn5P5Vp7KIudnMTaW8TEEfT3qAQMldIdastQGDH5Tnpg5GahkgXjAGPXFJ0V0Gp9zMgd1IIzjNa9su9Qx6/TpVQRYYjBFbWk2Ek6seePatIxsJu4ySNWX7vPrWLdMFLALyOldZPpzrGzYwRXPTW37w/LmlJXCLMsyvjgkE96kjnuFwQ7bhjBz0rSg09HOWHHvWpa6XbnjGSe+M1g6bvoXzGNFqE+NrZfJ71FdDeMnqfSuln0FNgMZK4521h3dq0UoU9cccUnFrRjTT1RhX1qYNp6Bhke9RRuqLkmr1/HN9mXecheBkdKyw3ABGTWTXKy73N7QtWFneP5kjpEwxkV2guC6ht2Q3II715kGKrle1dPpetRRWuycMe4IGTW1N9CJI7zS5C3mH0WodUfbcAf7I4qho+q2svnGMyDEeTuFV9c1i3hvQjuwbYp+6SKE/eCS0JjJ70wycVjtrlpjgufotJ/blmf43/AO+DWpmbIb0p28ge1Y41q0xnzGH/AAA0v9sWp/5an3+U1LZRr7+M4o3e/WssavaZ5mx/wE0f2tbNwJc8Z+6akpGmWGaQsKzjqlr/AM9x+Rph1a0x/rT7gKaBGluz/Smluazf7ZsznEp/75NKdYs14M3H+6aANDNLnj0rNOr2fTzR/wB8mk/te06ecfrtNAkzSGSetKRWeusWB6zH/vg0/wDtvTh1mb/vg0wNBcgUHjvVE6zp+M+f+ammnW7AdZ/yU0rDLpGelBGOlZ51uwzxcD/vk0f23Y4yZ/8Ax00CLpzg0gqj/bNjgN5wwe205oOsWP8Az3A7/dNOwi+BkUuB2rPGr2XacfkaU6xZcf6QufoaRRog470uc81m/wBsWZGfPTpT/wC2LLbnz0oAu9OvJo31nNrFoRxMvNNGqWuD/pCcc4oA0g/PFKWwc1mf2tbdPPTH1oXVbZh/r1/lQDOtMXpTxAPSrSxHI4qdYCe1cpZSEAPapFtvm+7xWjHbeoq3FZ88imMyktMgcVMliW/hrdisQccVowabn+H9KAObh00kgBavnSmZQAua6iDTFAywGKvLaRAYxQBxA0dsfd/SlOisf4ePpXci3jH8NL5Mf90UwODbSGGRt4qu2lkZGP0r0B7ZCOFH5VWksk5+Uc9aTBHBPp+P4agaz/2eK7SWyXHSs+WzHPy8UA0cq1tg9Kia2HpXQS2oByBVSS3x2piMcwj+7TDEMYxWlJD6CoTHQBQMY+tMMY9KuNH2xUZSgCqY19KTYM1YZRj0qMrQBCUHpTdgxyKnK1GRimBEUHpTCnOMVMRTJD5aNIeiqW/IU0B5/rMgm1a5degYr9ccVmE7YyTU7v5jlv7xLH8TVW4bbbv74Fdsvdgc97yM+f5iPfmoQRzUsh9+lRAdxXA9zdCAZ4Bro9A07zJPNdcquCMisewtmubtY1XdzyK9Gs7BLe3SJBkdzXRRhfVmVSVtClcHAC5x9KyrvUhaLsh/1p7+ldHcaW864UlD6iqieGIEfzGDyHvuPFdVjJM44GW6kLMzEHqSOtRXVm6zERozDqCATXdtZCJNqW/A7CqktnI/3YVU9iTWUqbl1NVJHHwWtyMt5Ui4Gelaul3hm/0eQkSL93PcVspYy92x7Vai0szSK2zcy9DjkVcYcqsKTuUUhZmA2nmuv0iNLYKDjnFV7fTI7fJZctVuNQHA6c1oiLl++j3QOuAOMg1xl5GVJIXnNdvcDcg+mK56SHzQ6Ywd1EkCZy15qBtMIOHIzj0rNOtXBcbXcDsA2K0rvSVW5llkMjlmycnoPSqn9l28jhVkkRh2AzXLPn6G0bFuy8S3ScSOZEHVX/oa1luYNQj8xPv91PVayW0P/R9kMgYjqXGM1Utzc6XOrSIwHTg8MKIyb0YNW2NmayS4tJIG/iHB7g9q4iQMjurfeViCK72GXzAGU8EZFchr0P2fVJyBhXw4PrmorR6jjLWxTibPynuKv2DB2KHr2rKhfDVoaaym+QdMmoov3kOWx3fh23wt0MY+Sqvia3xfZI52AfpW/oMA8qcgdY+ara/aBr3pzsFdcKf7y5Mp2icR5WOB3prRYIPGa23tMDhR+IqFrQZ6AGurkRhzmakec8flQY8c4rQNtjp1phtyCOvBrNwL57lQIODil24/+tVjyueR+lN8sYqOUq5A3TNMPXgdalZcnimuvfjFCghczISO1BUZ9acVBNIcUcorkfGcUhOOKXGc8YpuRnBFNQQr6jcil3evNKBSAAg0JILgSKbnk0pHbNJ24p2Q7iHnpSZpcU1gc0cqBsU/WkJpMHI9KCcmnyonmF9qOgx3pOSKBk0OKHzDunv/AEo/h64pvGTS/XvU8iuHMKoxnjjsaUrjGTTRnpTuvrQ4BcaME89KfgY5oUdu1P2g89R601GwXPb44uBxVmKHvipIo8nAFXYoeeRXknUMitwcCtC3s92OMCpIYBxgVpwQZpoBlvZjPStGKEIvSnRoFFSUxABiiiigAooooAKayginUUrAUZo8ZrPnj65rZlAx0rOnXj3paorcx5o/aqMkeK1ph7VRlWgRlyR96runtWhIuc1WdRjFNAUHWoWWrbjjjmoWXNMRWK8VGV54qwRUZHpQBAR7Uwrnmp8VGR60AREVn61L5OjXTjrs2j6nitJhzxWB4smEenRQ9DLJn8BV01eSQpOyOIPI6YqtdA+Tj3zVtuOQeajdA6kYz3rvqr3Dmi9TEb60wDr6VOyYZgV5zURXqBXnHTfqdX4Ps/klumHJbYv4dTXawofpWPoFr9m0e2Q8HZuIPqea1lcqK74KySOWWruX0VcDNSlFIxniqKzACnrcYJFVYkfLCoqnJC3VF+lXgzNz2p6oPSmkO5nxWDE5krWhjSFPlGDSDgdqgnuQgIzVWHcWWTk80+0iaeQBRn1zWbLcBELuaz310QErG556gGgEdbMFWTyw4Yj0rNmXyro5H1FYMevO0n3yT61ZGsC5dfMcFsAUMLF25sxONy9azJLMq2HTnPUVuW0oK7aneBX6ioaKTMCO1weCwFTSack8ZV1DKfWtMwBT0xTljwuO1TYdzEj04WylUPy9h6Vz3i6z3WcVyFwYm2OfY9P1rtpV7isfVrYXen3MDDO9CB9e1TON4gpWdzzKP72TxWjYFRdxMSQNw5HaqEXoRzV+xiM11HGATuYcVyQvzI3drHsGgx7Ypf8AcwDUWtoPtp46Afyq5pAXy2IPBQYqLWF3XOfYV6sF7xyyleNjnnj5I7GoGi9hitB0qBo+vFbmFyiYxULRYzjvV9kAqJ1walou5RMY54wahdABwMnpWh5e6ugtfCUOo6VHdRXUscjZGGUFcj9axlaO5cbs4kpSrY3NwrNDA8gQZYqMgVpXemzWl89nLtMqEfdPBB6Guq8P6XJZWsrPLFIJCCAjZx9aic4xVzSMWzzkgdajKnrXWa74agsLSW7iuGADZMcmO57GuX47DNXFqSuiWrEOD0zzTTg5qVhk9hTCpxRYL3I8CrFrYz3rlLePewGTzjFT22ly3ETT5Cwp95yM8+gA6mtrR7TyW8y3sbmRzwZZjsUfQVlOaiUo3OcutPurJh9ohaMHo2Mg/jVXBH+NekvbNc2zxXkcZRhyi5IriNXi02Jx/ZxlbBPmcEqPxNFOop6dROLiZhHA9aTOaUZxk9KCPetbWJuIQN3HWkKilBP4UoGRQC1GjB4xQVxwBQRjj1p3U+1OwtGNAx2qTg9aMCg0rC2E2n8Kfj6CjGQCKUA4HemUKEGelPZCOCCKAG4x1966rw9qKagfsF3Ej8fKWX9KidRU+hajzHrcUXSrsMWfemwRdM1oQRDHFeOdBJbw/dz1rTij2ioYE+lWgMCrAWiiigAooooAKKKKACiiigBkmMVnTjrWhIaz5myaQ0Z83eqM3Jq/NzVCThqBsqSDGaqydOlWpRwc1WfNMRXdRUDrVh6hegRXYAdqjYY5FTNxUZGfrQBCRntTCOalIpjZ70ARke1cb4xcG+tos/djLH8TXaEV5/4km87XLgA5CYQY9hW+HV5kVNjFbrTGIAOBz2qQjtTCOc9DXfNe6cyMubiQdKaq4JY9SeKZcnExGc4OKmU/uAe2MfjXlrc6eh6NZfLZxLnog/lT2lxVSylU2cO08eWB+lPkfNd6ZzssJL2OTVqOdc1mCQil83I61aFaxtJOAcYwaf5+OtYouimOCamSVpBycU0BoPcnkVRmlI5Y1MkW80lxYmaJgCFJGAaYjj9c1Z8EITgcAE9TWLBqFxIwDKGB4yBitW80i/E22W2LAHqOQfeoJdOuIUyYyoHOMVnLm5tNjSNrDPMcHlsY5pqauUlVFRuuMk0kcUkzEKOfSn3Fg8TI0sTKTjDAdaJOWlhqx2Oj3rTQAknIOK6KCYOoz1rl9DgcWm5gV3diO1bCM0fHNVYk1GdfxqJ3AGRVTz/U0x5vQ0rAPdwCRVV2+YE+tI8oNQtJgdCaT2A84uovJ1C6jHRZGA/Ouo8HWK3DyztgtEw2jrj3rmbx/Mv7l/70jfzrr/A29YrpiD5ZZRk9Caww8U6pdR2iegadhVYf7NRalgz5z2HNSWTfex6VDff6zt06V6aSuc0tjNdeagcVacZPFQmtCEVW46ioWB44qy/TpULdcis7DLOn6XcajcrFboWb+Juyj1Nd1fT2vhzQtz5McKhVA6ux9PrXPeG/EFno8Nwt2GWN8MXUZxir9jqMXjTTdShmtxHCGAizyQOzfWvOxEm5Wex2U0ktCu9jY+JLMXDWTQTSpuEw6jsMnvVbTNAi0aRHnu2aX+GJCcfl1NPtpvs/jS501HKxrZrFH6K23ril0CK/ghuba4VGmhnYSTyHJIPTHtWMpStZmiS3L93HIVJSGOUqMhXOOfrXmut3Tz6kyPYRWskRKyBOcmvT7mNHtyX3sg5/dk5b8q801mwmtdSml+wzW1tK2Y/MO7J781rhtyKuxjuMHOaYc561O4qPtwBXcznua+la9JYQ+QYw6Zz1xiulhurm70xbiOLbPLny1Y5A56n+dcGi7mx3PFdN4mupLCxs9PgcoXjy5Xg4HGPzrirU7PQ3hK6Neyha2yk169zO33j2H0Hao5ftUd5GixQGyf5XKj5gT3Ip2lR3osYVazjgAUc7sk++KlvZLWzVLq5kKopwCORk1zp2ZbRyPim0s7W7iECiOZ1LSKvTHY49awO3Suj1rTtQ1Gd9Tig3W5UeWCfn2jvtrns4GK9Gk7xOeasMx2IpTyKcc0mecZq7EjcEc0oHIpcA0ox0ptCG8huKXbmlxz0p4IGRg0gADC9KcBjFIW7DvSquc5zTHc1bLRLq+VZIgmw9SW6fhXUaN4Zi0+cXMspeXsq8KK4i2ubqzmDwSlT2wa6S1ute1iEW6OIkbhpQMcd+a46/O9HsbwS6Ht8MeDitOGPoKrwx5FaMKdK4TYljQLT6KKACiiigAooooAKKKKACiikPSgCGY4FZ07eh/Cr03p2rPm60DKkp/CqUvU1blOc1Tk9O9AFaTrVZ81Zc8VWkOQcUCIG5qBqnfjrULZoAiaoz7VIRTD0oAjNMIqQimGgCNuBn05ry67lM91NJjBdyc/jXpWpSCDTLmXpiJv14rzNwcc9q7MKtWzGrsQYxn3pOh5HFPZSBnik7ZIrta0MEYt2uHzj7xNLCn7srnvnmugv9HaS2jlQYWKEMT6k1mWKIXkSQ4bb8o9a86dJpnQnodJo8hk09A2dy8GrrHBIHaqGi5NofY9KvMccV0R2MZbjCST0p3TimkY6HilVdzcirEOBzwa0LSLcc9BUEMXIyOKvhwi+mKtAWU2ovBpTJxxVB5x2/E1DNqEVtCZJH6dvWlcVjRkeKNC8rBVPrVK4uLSZMm3kx/f4H6VyV5rc1xMzluAeAD90VUl1WZmz5jY6YJqlFdS7M6dIrKCYzCQem3ZzVzzYJioA+Y/3hXER6lIkmQ2cetaMWquy7WPTkUOKKszr4zsPbFWXQOuRWHp2oLdw4J/eL+orVhlIUqTxUksilGDjkioS9WZj3FUmJzmpYhS4Py5qvdzmG1lkAyUUnFOx6Gs/WpNmkXLEkfJjj3qZOyGjilOWG7jJyTXpfh1EgtT5X+qlIkCHtxXmZI2BSa9P8OOzaZCshUssa4I/iGOtLCfEx1djpbMnnjtTL0/OKdaYBP0pl4Pnz3ruW5g9im+MGq7dKnfpioT0rQzIDUTdelTMM1C3pUspEZPYjg11Pgohbm4hTCqYt2APeuVf71WLLUJ9OnE1uwVwMZI6+1c9eDlHQ2pSs9TtZrK1n8QtL9jH2mLa32iN/mPHRh6UjWaG9u5wYbgybcQl8FSBg5rgbjXb+68ST6gha0n2AKEPy8cfjWXGLz7TPcS3LpM7Ft6n75PWuH2NR7nRzxR6bdyXo0q4aG2MN2qN5UfDZI6V55qF54ivofKvYbhYwwYqItvP5VJFrmt2w2xXxYD+9zVuLxpqsS/v4Y5R37GqpxlTd2gk0+pzskUqLmSNkPupFVyOOOldFc+Nr+VWVLSCMf7QLH9awZbmW5leWSNVZjn5FwPyrthLm3Rzyj2LOkW32nVLeIgkFxn6DmtLWrq0fxaPtiGS2gUKVHc4zUXh69tLK8ea5O0LGSp9/8awpZ2u76W4kBzK5cj61hUTlJmkXZHcXVguq6Ustg0tqx+5lioYe4qwkQt9INuDHdTWseSDyNwGRWRJ4tWPTVSKAm5AC/wCyoFW9E1uzuncNElvcyHc5xw9ccovqjVMna4uNR0QPYTxi5dACz/wnv+NcLe6dd6dIEuY9uejDlW+hrSm1e60vUbu1tZ1EKzNtAHAqLUNcudQsvs86RH5g24DkY9K6qHNB2a0MqlnqZWM0YGaA2TgjFBBP4V12MQABOQDQRjmjHGe1Lgd6EAmM9DThz15pB3AHNKMgc0INgxzmpOnfH0pnAI4p4GRmnYaEBPJxmug0jxSunWHkyxM4ByuO1YAXB6mnlVxjHHvWNanzlRnyn1Rbx4I4q+owKjiUAdKlryTqQUUUUDCiiigAooooAKKKKACkPSlpr9KAKkzGqExHarkxznmqEpFAFWRqqOfWrMhqpIc9KAK8hOagYip36Gq7YxigCFsmoWqdulRMM0AQt71GcVKwFMIoAjppAp5ppoAwfFc3l6KY848yQL+HWuCPPeuv8ZyjFrB7NIR+grjyv516WFjaFzmqv3hmAQcDPrTT6daecDPrTACME10mR1tt5TWENvL1kjB+uKo3Wg6cFaeGKVJVBIAfgmrun6hb/wBmwlyodF2n14qK91m2WF0DDcVPSsZIuLsUdCGbKRuOtXTVXRCr29wV+7vz06cVZZsN7VktAYgBJ6VNGuGyajDcg07PzDFaIVy9GVxTZZcZ5GPeolfAGTVK7uBt2nJzTEytqGpmHKRdR1Oa5m4v5rl9pYt2AA5NdDFYRTHM3IPX1qaMadpny28aq2clm5b86EnfQuNjlhYapKD5Wn3L++wikbS9SAHmafdg+0ea7FPExt02kBh69xQ3ipAcbSSR9Kp0my7nDyQXELYa3uFJ/vRNSRzup5VwB6qRXfReI4JYyJOg7GmyXmmyJlwPYgUlTa6jucnY37QThwcfSuvs7wXEYYde+axbvTbfUGH2cLby/wB4DhvqKsWsD2LIhk3DpmpafUmR0O7K8jrVd+eAaSOQletI570jMhYEDrWZrozpZXruYcVqHkY61TvoftCRr2DZpNXVhrc4Zl+ZF28njnvXq+k2gstMtrdh86RgEnrWDpXhyIXpvJfnROIlPf3NdXkHrWuGpON2yask9i7adT9KS6GWH0pLXii5IyPpXT1MuhUNQuuDipmzUDdaokhYVCxNTtyDURHFIZCR61HjccDrUj9MU1eD1qZFRJr7SLuwtYbmdFEU33SGz2zzWYYy7AL1rv57CbXvB1tFAyCeNgQXOB8pwcn6ViJ4R1FGJ32wx/01rnVVNXe5q4O9jC1LSbvS2jF0FHmjK7WzWccketdx4ztnOhW07EGS3kUOwORyMGseLS9PstJGp6rLIsTnCRp1NCqxcbsHBp2RzT9CMc1Eyt2rpY9L0rWo3OkzyRzqCRFMc7vx7VBpmk2sizzalL5KQA7k3YJI6gU/aRtcXI9jnunGMVattHu7uCa4t41KR/eywB6Z4rZtI/DuqXAt4EuoZH4V92a0dDt203V7vTZiHDKHVsY3D1/KolWj0NFTezOOisri5id4YWdUG5yB0FPsdLur5JZbUKBEMtubB6Z4rqtE8iyvr3R2EhlLsST93Z2/Q1BpMtnZeIZtPt/NVCChWU5+cc8e2Kh1l2HyM4wx55xznpTu/Wukv7bw/pl69tc/aZJT87bTgIDyBVHW9GSwWK6tZDJbTdC3VTWsKkZaIiSaMYn5vejOKXHFG0FeufrWtiBoyMccUp60u3jgikOcYFCAUHjinA00A4waUd+KLBcXFOz0zxTM46U4HHahIV9S7YWEmoT+TE6Kx5G41qjwrekfNLb8dfnrF0+7e01KKXdwp/Sul1zSrvUZIrqxLMHX5l3Y/HFc06k1KxtyK1z6OAxS0UV5h0hRRRQAUUUUAFFFFABRRRQAVFIeOalqvKeKAKcp4NUZTk9atysKoynmgCtIc57Cqr9TirMlVX9TQBA/X2qBuDUzEVC+aAImNRsKkbGKjNAEZFMapD0phHNAEdMIqQ1FK6xRtI5+VAWP0FAHB+LJxLrciZ4hRUx79TWA3B4NaF4HurmW5PPmMWzVUWsjcgcV61Oygkck3dlY5yAelGD71Y+zS56UhtJs42kk1qmiGQBypwDjPFMmTzUcZwxHWrP2WbGRGx/CnfYpRwY2qZJNAmT+GMpa3kbnLLIOPbFXpX/eGqekxyW17cxOpAlXcMjuKknb94RXMlZmj1ROkvqetSbwCCTmqAkGcdc96VpiQQpq0QWJLok4U1CXycsc/Q9KrgZ+9nJpQSxAB6cVYWZa3KFOG/CsW9ZpG3DOPetUQFhjBoOm+cu3GMd6BrQ5dpGXg8mo2uGHQVvXWgyBTtIPvWe2kTDtn3Heo5pXNNCmJz2yDUkdw/r0qb+zJS3A/OrkOhzcF8ChVJ3CxJY3L7wWGR29q2WcMARyKz47PyGwas5KpjtVXvuS2W0l+XAzmns545qiJCCCDUizkj/GpJLG/I4/Cq811HBLH5hAVjjPoaPNHbOB3rT02O3khPnkbWbvHuqorUV7FuwdJLcFGyB6Grq4pYY7BF2idkHosPH86sKLDvcy5/65f/XrrVkjFvUW3+9+FLODxUsJst3FzLn/AK5f/Xona14PnP8A98f/AF6LoCiwz0qF81aZ7XP+tYj/AHKiZoD0dj/wGndCKjA1AwOenFXG8on7zY+lRMIyeGbH+7Q2gKbjmm8gg1YZUJ/ix7ioinpmobQzrNB/07w3qViCdxVgMHplf8RXmEUV+yqxkkxjHMp7V0q3N9axulndPbM+NzKOcVQis2ij2s5dsklsdc1zqnFN3N/aaI6TR7eW98EX9pJ80gLlec9sj+VWdMv/AO0PDcUdosEl7FENsU4BDEVzQuL+GBorW6eAP97Z3qlDb3FsgEcrKynIcdRWDovozVVI9TobTU9fmvFtk0iGCQnDuYdoUepNMtLGC48WXkWoSW88iKJNqrhSx7YrLbUdeaIxnVJ9vtjP51mJp9yjmUSy+YTkt3P40lTdmHOjpJ9S1yO9a1tNFjgw+1WWLII9c1Nr++x1jS9TGQgPkzY6YP8A+s1gPda2Y/LGp3ATpgHn86qNFf8Ak+Q9zM0I52u2Rn1pezYe0R0etAad4qsb8cJMNjnsccfyIqvrdhc2viOPVYIHe3IV3dRkKRwc/hXP6hd3twIhcXUkyJ91W6KelN/tXURAYVu5fLIwU3dqpU5Dc0a/jS2UXtreKMrNHsJ9xyP0NWIEGp+C5IQcyQA7c9cqcj9K5i5vLy6jSOad5ET7qseBTrW/vba3eKCdo1c/MB3rVU3ZeRm2tSqp9TinHnmmYO/5uaeRjFb2MhO3Io70p5o68EVSBiE+hpwPrwBTcDHSl4YjigQo5GaVT82AeKMAUuAR70hjZARtYHkHJrsdCu5b3RZrdZCk6A7HHb0rkDkjinWlxcWsuYJGQ9ytYV6XPsa05W0Z9c0UUV5R1BRRRQAUUUUAFFFFABRRRQAh6VXmPUVO54qnKeT6UAVZcYqjJ16VclPWqMnPagCvJ0qrJ1qxIarOcigCF+OahPJ5qRs89qjbHWgCNh2qJh61KelRmgCM0w+tSMe1RnigBrEdKytclMWlTkHll2j8a1CK53xVJttYos/fkyfoBV01eSJk7I4qVpFlVIpGQ5520jNcDkTvn1pOWuGPtipOOhB+tegjlvcj3XGP+PmT9KaTP0+1SHP04qQj0pvGcdKBAGuQBi6kPfrSGW5zn7TLke9GMH+dA6+uabELHNNHLG7Tu4B5BqxecOCOhqsSRxgUouF2rbs/z4+XPcVElbU0j2GBh6jNL5gBHpUMnXHpUWcHOc0KQNGjH857deKuQwAZwBWTFcHGKvw3SgYZua0uTY0ViH4VOjqnGBWY2oKqkDmqcmp7TxwfWhCNm4ukwTxisma5Vjwcis+W8L85NRecMDnGaeg7WNiylSNcSHJ/vGtRHiZeB1rmEuAGAz0q/bXq8Bj0pWRTZqyRD+EcVXkhwvA609bgOFOR9Kc7qy8HH40mQUSnGOhozyCOSPWlldT0P1NV2mAbAI+tG4yxndwOp6Ct+DQt0SE3txG2OVTGBWTo1sbq63t/q4+T7ntXWIQoAFUkRJoz10LJydSvB7ZFSjQc8jU7wflWip5zU68iruQihb6Ftkz/AGndk++KW40IsR/xM7wfTbWnEecinSEEZzStqMwW0BsZ/tS9/wDHaQ6Ecf8AITvfzWtknFRk5pkmM2ikZ/4mV5+YqI6O2Mf2jeY+oraOKhekMxjpUg66ld49sUxtNkz/AMhG7/MVrMM81A+MmpY7mU1hJkf6fd/TIqM2LrnN9cn8RWk3IzUB9aVh3M9rSUZ/024/MUw2smf+P256f3qvNnk4qJsgUmguUmgkA/4+rj8WprQSEHFzPyf79WmGajOKBplRonJA+0T/APfdMMTI277RKcDoW4NWicelRngZzQguQ3uRbhk7cmqcb7s1piPfCU/vDFY0ZKtg9jitojLAHp+tIR7/AIUufl96DwKuwMjx6nNGVzwTTs54JppAP1pkbbC578Ude2KBzx0o6nFIq4Z5IB+tKBwcDmkCdeRS4xQAchsY6U4jOD0pnzGnbeOTRYY4EU4HBqIjFPUrjjrRYVz65ooorwjvCiiigAooooAKKKKACiig0ARyHiqcpqeZj0qlKSM80AV5nqnIankOaquTigCCQ9qruQfpUz1A3NAELVEckVKc1G31oAjNRmpDUZoAaajYd6lqM9MUARkVyHiebN3HEeiIT+ddc/ArhNam82/umHIU4H4VtRXvGdR6GHGFJYjuacwAxn8KbEmEBxgnmlwCc549q7TnBulMUk84pT+NHIPIx7VQgI4yeab0peaAcg569xRYQccDvVPUYQ1sXUbZU5VqvQxPNOkMSlpHOFA71vroNpdWVxaF2F3F9+RfU84+lRUaSLhe5yVoHudPFwcMyttfHrULFl7de1XbS0n0+S+sZ1KvGyvge9RTAN0HOaxi9C2ioJOfpQZj2Jpkqnd6VGSematTFYe1yx68elRNOxByfrTHORUeCelVz9BWJPNxxn3o80nn0qLaTSEVHMUTrMwGPzqRLhh0NVenejJzwecdKamFjVhviCOelW1v9y5yOnSsIE4HPWpA5B4NUpiaRqy3me+Kl06xudUufLt43kOMnaM7R61lQRtK+M1618K9PddRuJFRo2iT5XHAbPUGhzsChcoWNslpbLEoAx1PqauJz1pb592o3LesrcD60xTx9a1RhIsL0qRT71ApxUgPvmqILCcEZqRxkVCnXrT3OBQOxHzSHij9aaRTERtx3qMkY61IRUTfpUgQtmoW4qZjj3+tQtyeOtBSIG561CwB4FTOefeoW9SOaQJkTZqB85wSanJ4qJh70guRNjODULcCpmxwTUTAZ/lQBHiozjFSse2MmmNkcY5oGFucMQfwrLu4xHdyLzyc81pRkrMp6jpVbVY8PHIO/Bq4vULlVMlcjindabERzTzxz0rcQ3n0yKQj35p2SB7U1ic0gE7+/wBaXaRg+vejcCORSlhjGaQ0xMZzTgex5puVx70pIFAJgcZBNA9aAQaXkgUDF5PNOzx15qMHnn8qcTnnHFDFc+uqKKK8I9AKKKKACiiigAooooAKax4p1RyHigCtMapynPSrMp5JqnKeOOlAFaRhVVyMVM55yetV3PNAEDGoWxUrYzULUARHrUZqQ5qNqAGHmmH360/GTTSOaAGEmoz709uKaRQBBKwSNm9BmvN7590UjcZck8+5rvtYk8vTJz0+XH51wF+AERc966aHcxqlIDaAB+VOzz93HrSsqlepqMg457j1rqRhsObkdqaKT8MCg475JqhJ3YpBB5NLGrSuI0Us7EBVA6mkTLsqIpZmOAB3NdvoWhDTkE04Bu2HPcRj0HvUTqKC1KjHmegui6KunxbmAN04+d/7g9BWr5KryFCn+8ByamXGOBim+YN2G49M1wym27nSoJHK+JNOPmNeAHLptLAenTNcVKQrAgn616hrhQ6VPGx5dfl9zXmV1H8xA4wa2pO6IloU3bIPFV2NSsMcVGR3HUVZBE2cds+tNxx/WnsNw6c1F0wDSaGKx9M5pNx4pp4HNJk0APLc0vfBFNByOTSjrxQhkgwR9KVcZ9aYDViGPcehqkIv6ZCJJVyCQOcV794HgW00FW4/eJv4rxDToQoU4x/WvZPC16p0GE7sskLAj0IzRJbBF7nISNvnlYnq7H9aep44qsDliSepzU6GulbHM2TL71Ip5qIHPBqRcAE0ySdTTy2Rz1qBGqU9OtIpDccZrR0bR5NYv1t0JWMDdI/90VRjjaWRVQFiTgAdzXp2h6T/AGRpiwgD7RJ80re/p+FZVavKrLc0pwu7vY4C68NamjXMsVqzW0blYySPMcD+Lb6ViOCDgggjgivWIXlg1m7tZHLrKomhB6gdCBVTVdCsNUBMsWybtLHww+vrWcK/SRcqPWJ5YwFQMPSui1bwxe6bukQfaYB/HGOR9RXPNjaTXQmnsY8rW5C2OmKrv6GppD6VCevNMkhYUxhipGxn0qJjilYLkR64pjccdqeeajbvg0MLkZ4PBphPJA/E1KcdqiYdz+lIZGxxj1zUmoRebYM45YYaoz09auWpEtsyn3WmtHcGc9GxVsgjGKsHng1WdDDKydShwanDdGroWoXF6EimnJGSDS4Oc4ozkc07Idxp9s0g96eMcUHGaNBWsMOOg607HHTJpMcZxS5yMmkIQc8ZzTgQBim8duKORmgeovzdetPGCOlMGRinbsDpRYaZ9dUUUV4J6AUUUUAFFFFABRRQTigBGOBVaaTA61I7d6qSkE0AQytVSRuDU8jY75qq7Z70AQO2TUD9KlfP1qBulAEbd6hb2NSMcVEfegCJuaYeO9Sk1GcZ96AIzTW6U9uKYTQBG3I5ppFPPJpjUAYfiSQLaJGCMu4/SuIv2/0mJfRSa6vxBIXu4YwfuoSfxNcjdHdfynsuF/Ku2jHQ5qj1IieuevamY5+pp+dw/pSHvit7GQwsWOMU4LnAAJJ4AHJpQfmx3PpXY+HtB+yKt3dKPtBGY4z/AMsx6n3qZyUFcqEeYd4f0IWCC5uVBuWGVU/8sx/jW7yDjBpSwHHfvmj71cEpOTuzqjFIDnqKNgkXDDIHTPagcHk8etYV94y0uxLKj+e6HaQp4B+tSUbFxCDaSxt825GAzyeleV3ibXI9OvvXTQfEKK680/2cygfKreZwTXO3vzynAwCM1vRMptGQ4AOQOlMOCuO9SzD/ACKhxxxWzsZjTjPJ5qJwOMmpGGfrUbZ/CkxjGB70n86f1pp+U0tQFABpcZ7c0A8e9SqmTwaACJMmr9umCAR+VQxRdwMVpWyAEcVrFCZpWqlVB6dq1bTVp9LdRHMyQz/unwehPQ/0rLhG0DninXqebYXC9PkJXHYjkfyqmiL2E1fWrvRZFd7Zbm0Y4EifKyH0I6VcsvEKXkKzQqssfcA7WU+hFQyoup6Yu4AieIHn1I/xrhIJ5tI1AqD0OGB6GolOUQUU0esWt7BdDEb/ADgfMjcEVbGRXC210LvbNbymKfhgQePpW3DrVwV/exgsPvYHQ01WXUiVNo6JTzUucgCuYl1a+kGLWWAMegZKrPq+pQoTcXQMuOEQACm6sQUGex+DNGDv/ac64ROIQw6nu34V22fXqa+YbfxJdvIvnXczKDgAOcAewr2nwJ4tTVrZdNu3zdRJmOQn/WKOx/2hXFUbcrnVBWR1Nzawy3ttcOdskIYJg4zmm3MSuuC7A9iDViUeZlWj3Cs64tC0LoJHaNsjaThl+hpIsru0sIyRvX++v9RWDq/h6x1NTKq+RO3/AC0jHX6jvXRQGIKUjdiR8pDcEAVFNGigsTtx37VUZuOwnFS3PLNU0O90w7pY98P/AD1Tlfx9KyHAI4NevynK7eCG9OQa47xRoun2tm14mLeTcBsH3XJ9u1dVOspaM5p0uXVHFt154qF+lWHAqF8dhmtzEhIz7VGyj8alP4VE1AhjHpmo854xzUjfpTTjqBg0mNETfTmp7BsStF3YZFRHOScUkDGO4V/Q/pQ7DRS1aHyr/dghXGahjOUxzWxr0G62SYAfK3J9jWJE2Gxit4u6FsSnI7/hSfjQeck5oGO4q7BcQdOtIOuTTvloySOgpMe4LnvTSevP0FKc5owegpLQQpGFHSkzk4JpMnHtTuAOlAAO470EdzgChTninEZxx1o2G9j66ooorwT0QooooAKKKKACkJpajcigCKQ8VTkY1PK3NVZG4PrQBDIaqScip3POKrOc/hQBG2cVC3r2qRj61ExoAibrmom+lSsO9QsaAI2ph5p5Pemd8mgBpFMPSnnnrTWAFAEVMY45qQjioZTtGe1NCZyepuZdUnP8KEL+VcoHLvI+fvuSa37uXEFxMep3NXPpwg45rvpKyOWbuxSvHAFN2/hS5A4rf8OaWLiU3k4Bjib5Uz95vf2q5yUVdkxV9C74f0RbfbfXaAzHmJD0Qf3j710XmhTlhlT/ABVVG4FzMRknrnjFQ3eqW1hbl5JFbA+7nrXDOTk7s6I6aGmUB5FQT3MFrGZJZAgHY9/pXJQ+MJ3ZpIoESH+FCMk1nX+ozalP5sshJ7ADAX6VFi+ZGzrHiYSQyw2qMseCrSnv7CuAl2ecAYwST2rWdwyLt+6n5GspiDcE44ycUEtjJJQm5xgBQcADgVoCUywxMRyVH8qyZOYmXGcirltIXsbdvVBW9Hczk2Eoz0FVmXuOKuM2RjrVd156Vs0JFdvSo2yODUrYAwKiJ454qRjWOQKUA55pvXjtQOOKkCRV+bPGKsIvzVFEMkZHTsauRoO4OM1cVcT0J4UJOD0rShG0fIOnGapJ0IHFWonG3pWyRFy3EeOKknfbaTFjx5bfyqJM/XNRarIYtJuCRklNgx6nigi5f0of8SeyyesK1y3iezC3jSgYVq7K1j8m1ghwB5car+QrG8RwCSNWIz2qJq8bFxdmcppt+bSdUf8A1bHB9q6j7a0bAufk4BbGfpXGyxkMc9q2bCYz2O1skp8p9x2rk1Rq9Tfldip+zjII5I7VnTyFUOeWPv1pYmIB+ZhuXPB6+1Vb6ZggVUILCgQsVw6/JEFDD7zkZ/Kr8E15ztuMOOc4x/KqMAA2p09ass5jGxOWPU0irs1otc1VAVjvLhHXhgsrEfUVuad431+zdWW/klXHMc3zg/nXGBnAVwzK6nG4d6mS+njIwI2+vGaBXaPZdJ+Iel6iqpqkJtJv+ei8rn+Yrpvtuk6nbmNLyCVX42rJg18+jUQ/LWbAgZJUg5qVNXWM5AZeM4KH+dIpTPe4LaGCNUiU+WvC55z715n4s1JdX1vy4mzaWJKr6PJ3P4dKybHxpPAuxL2VY2G0ryf50lvJZzoPs8gA6kVtRsndk1JNqyIm6+vtUJxz2zV17Z1GV5FU5AQCDkEV2K3Q5JERwBzUXXPSpDyKY1Mm4w5z06VETyc55/SpGBqM8EZoGhhIHAz+NMYZPtT25PXFMbI4PP0pFLQ1ZEF7pLKRyUP5iuVQlSOx75rqNKlJieLn5O31rnb6HydQmjxgZ3D8auk+gMcOvNNIpUOQPUUE81sIaQABSZB6A08gdAaTp1FMYmeOtKMEdTmkIz0HNJk4wRSFcd9KOpwaWkOPc0gE3EHGeKdwevB9abjByRmnLz2qmCPruiiivnz0gooooAKKKKAEJqCQgVK5qtK2aAIJG5NVJG5JqxIeT2qs5460AQOagZqmkPNV2P40AMY1C1SNz0FRMaAI2OaiY9qkOO1Rtz9aAGGmHPUYp5FMIzwKAGmmNTzjpUbdetADTmqOpSGKymZRlghwKlvNQtLFf9JuY4s8YZufyri/EHiaZ5RFaRP5DHAfH3qa3ExtzGjWZidyoYDOOtYbtCSTHIXCnaRjkGr9xNuI6561kBRb3su2PKTsHOex710wq2epzyjoWI4ZJTuC8epOKv2Qu7GYTwyiNh1B5DD3qjLdxL985K/dUdKoPdy3koTLLGD0U9BTnWurWFGNmbviLXGnARJCgVeRGeM964u8vJZMoZWYEYJJrQ1EqI+SaoWdmZMu+MelcxtuasEhMCJbpkogBdzgD/GnKjSA+ZKzgHoOAaau7GFO1ulCyOjbXQbfWmiCaVQkQCjp0ArOcYI6da0JGDRnNUJ/l/nmgNys4y3Pr0osHIgaJjgxOVA9qHBZic9RmordvLvSCcrKuP8AgQ6VdN2kDWhc3HJ4oY5GOaRiU4JpoPGTiuohDGAPHaq7gA1ZbsT3qu4561LGRnOKFHIz3oI5xnPvSjg57VIy1FgDpznrVkH8h7VUiy2ePwq2q5zWsdCZE0b4PcGrkTZAyRVBQxHNWYcjg81omQaKEEZ9KhuMXWpWNn1XcZpAfRen606NyAOmB1NJoUZuri61FwP3j+VEfRF/+vUtiSN0H2rO1ZN8OPxrSUE5BGAKr3K7wcDigDg7qECQkin6b8kkiYPzDNad/aYYnYSD0NZ8alJgTkEcc1z1I21NU7ovo2MqOxz+dVLuX/SYw3IB5qxC/wC+Ykfw1Sl/eXwAzt649qxGkaVsmUZyenagIdxPvT/uQoMYJ96VBkZ7Uig2lUbnH9ajAHXBp7feXpz0GaYc52dc00SSxkgYJ5qQy8k55x26VCRgc06P5zzmgCVBIqBmPHrmpgEchmU59QcVDK21MDoeKfEQYwOwFGwidJJoT+5uZk9t2R+tWxqk21VuI45eMZ+6apoQU68UycjySdw4qlJiZd+0wvjfE8Of4s7lpG4PXI7EVWtpAsIJ4Aqxb7ZYiB65Fb062tmZygugwknjNRN19qndCrYPFRn0rouZpETH5cn8KZuPTP4VKRzkion5JBHHbFAMsafIY7oKc/OMVDr9uFlimAxn5SfX0pis6OjLwQQeK09UQXOnMwzkYYDFOLtIaRzcbcbe1PI2jioUID5H51N1rpTBoaMUp+XjGc0dDjAo2nGaGJCN04o5xyKU8YxSc560X0EL24owSAc0EnGKRSMUrDF9OaRjheCc0vQ+1GRnpQgtY+vKKKK8A9IKKKKACkJoJqNm5oAa7Y61UkbNTOcVUkbk4NAEcjZ96rucU9mI6VCxoAiY1E5zUjE1C5/GgBjGoiecc09jUbHjmgCNjUZNPbOaYaAGHrSGg4rLutesLUlWl8xwcFUGf1oA0GGenasHWtW8qNoLeXD4+Z1PT2FZl74gluwdp8uPn5Rx+dYU135mQDkUCuihqFpHcli0kob+9uzmsoX91pv7uRiyEYV/8960ppGD8k461VuYkkjwcMh6+1Mgkt9RFyrc8LjPFOnO+PMZz6VzuJLKd9rMIn4Iq5p0xR3hc/KRlTTQCuZG+Ud+p9KlgDJ6E/yqaRwuMDg9TTWClDtHPtQTYq6iN8Se56VPZxAwLjHH51HclWRFxzVi1j+QHGKEhj2Ubsk49KA4cc9OlShTsIYg+pqFwyj5ehPNMmwSruj6nA7etVLs7SOPariHI5JqjqLYkQdTmgdyNgflPYcHFVriJsnBwyncp960dgZdvQEVCy7kG7kjrQtGMej+fCjjgkVEVK8H1qewEbgoMA5yBVmaDv1rrjrqZMzj09KhI/P3q80ORUTQcD1qmhopFSe1KEIHtVkxY9eaWOMscdPapsMbGmDkjircQz25NPjgPA/GrcVtnA7+1aJaGbepEsRPXircNq2M5xmrdva45KgVejjRBwOvrVJiZhaorw2ojjP76dhHGPUmty1torC0ht4+VjULn1Pc1lAm98UE/wDLKwTjHTzGrYVS55PFLdg9hTIzDaBx7UojLqeM1KqLj0FSAqo9KbEkUprNHTlQa5K4j2XjLkED2rsrm5WOFj6CuKaUSag75AAz1rGp8JcUNQ4bGOCCODUVqnm3jEE8DGKdkYLY7HvUtkmyMvyCa5Ea2LMjE/L6U8DauS+OOOOtVd4MhNSs2W9sd6YgcHeR71Lkdec0zIcZOacmFjJz0piaBm+fHc1IMIuAeccioIwJZsngL61K/r0OeaTBbCs+UGelPgYhDnnFQP8AKdoPNTIucdiBQUlYnMoGRxz0qOWTdEQfypxjQoNp5HWobllGADx70IVrjw4S04AyasQERICD2596oy82yqOuanhLiEITgigmxorLvBDCmOCvUcVFExxz+JqYFXO1zxXRSq20ZnKNyKowOc/r6U+RdpP6UwnjGea6/QyGHAPWte0JlsAjdcYJrK6Ic9TVrS5TuljJOCNwzUvuUc/KhhndD2Y1MGBUVNq8Qjvi/wDeA/Oq0RwOtdMXoTqPwQeKQdB1p5wetNxkcHiqHYU4B6008ZyOaXFICM47UbgJnA5pc8ZxQcZx2pQOfrQxCdTk0N8owKNuDz+VKwBHWhCZ9d0UUV8+emFFFIaAEJxULt705j2qFjQBG5PrVaTr2qdm4qu+ccikMruKhbNTOeKgbnNAET+9Qsalb0qJ+OKYiJutMbmnnrzUbdaAGHioZJEijZ3YKo6k9qx/EWvLpRjt42AnlG7J/hFclNqdxcgma4aUNzyeKBXN7WPEGYmitAQDwW7n6Vyc0pb7w5plxc4XJYn0xWXJrAR9sg74zTRLdy68zIR3Heqdw5jIdSdp7U4XUcqZBG096Y6ho2A5zQJIcSJFyOKpmTy32seCMdKlt5Cyew9KgvRhtwzk+1MRHewrJFlRnI5AFZtrKRKFdsMh4PtWjGzPDuPVaoXtpukURg7zzxSH0NaTHl5OORUULZY5PFOjQtBtf74GPxqCJtr4OOtMXmSld3QDI6+1WYBsHvVSOUJMyP0PIq6rKQCDTESkFl7LimOoC9MfWkMqKMlqj85JflDgimIfGAQfWsy9O+9ReOOTxWkuVViCMAd6yGcy35OenFIZfUArzmoXAEm85OeCPT3qQnCDHajIdMjnHahAUm3QzCSM7SDkVuWNzBfxheFmH3k9fcVjMBzn7p6e1QHfDIGQkEcgg9K1pz5dBNXOjktgDyO1QtCAP5AUlnrFvcIsd3hJOz9j9a01t92GADDqCOldKlczMc2/PQHNOFvtx8ufatr7IMdAe+TSC3AIO09MZp2GijDb9PTrV+NFQ8dKcIiOOBTTkA4OKLiZMGH1pl3eJZ2ctw/KxqW+vpUIf5sAc1l6oftt7aaapJVj5s3+6O1DYkrl7RYHgsA8o/fTt50p9z/9atNXI5zxUKHjOMD+VOJpJ2Ha5L5uOhqN5ic8k00sx4psg/d8daLjMrU7gmNgTxWEnEckrdTwK09SZehI9TWLPcbm2KPlFc9WXQqKHLmTCjOSf0rUYGOBUU9B09Kp2ERKiU9Bwcd6sSNvfd2HrWSKY2NWzx1qxtwuc8VHFwM7e9KzEEjbz9aBD4tzEgHBPOKWQhWCseTxSRDDZBOaSRd8oBxxSuA5cKpB/D2pwXKcnn1qOQgMAeKeScYxTuJDcbpscnHXFW8AjkYOKqQjdIx59asMRx6dc0DY4naCwNUnbe+M5Oe9LPIfMwhBBHOKjjVvPGMGgLWLFwQQgK4OMirEJLKD3xVS4J3qPSrMb5VSOMetIdiwvucUeYS2AeRUcj/Lxx7npSxg8nimSy3uMiAEjIqvn5sAdOtOjZtw5H5UsqgNxXXRqXVmYzj1IieMU+2byrlGLDrg/jUfTtjim5JOcYOe9b9CUWdbhDQiUDJQ1jRHt3zXSTAXNhk4YMnP1rmACG6nNa0ndCsWe+KCcDA6U3OF45NKeBya1aBaCg5oAA5NIGwMmgnPUfSkO+gE/LTQCV68+tBB6AcUu35PegWrDcQBmjaG+lJsOOvNPCr9096APrqiiivAPRCmk0pOKjc0AMdqruxJPFSMSD1qFjznNAyNj9ahY471I571Ax59qAI3NQMfTipWHOc1ETQBCxH1qJqkaoyOtAiNs1XuJ47eB5ZGCooySapa3rcGjxrvUyTOMogOOPU+1cLqnii81BNj7FjByEQcfiaAvYi1ydNRv5LpgQW+VQT0XtWJkxYUE++ale+VuSKhMqPkLgYpkNiSNvGB26VVkgilBDrlvWnvJtwOlML+lAjOYPYS7HBaJume1aEUuV5bgDrUNwqzxlW/OqltI0RaNwMjgUAXIXWGSQEkDqPelldZo9qZLDpVSYgyA5PNOg+WUg/pQA2JwpKkHNTWaEzmU8hRgVUY7JyMcAnt1rRtQqWhJ4ZjupiIoGZmdT94MRVRuJGPvxU4YrOTg4b1pk453KP06UgEuEzCkitkr1xRDKCPvHFSW5whTjn1qrJEYpiB0FMCwz7gfSmAYYkdM0incKXLY5FMSJzOWgKjIz+tUYlBlZmPOasHJUAZ+tMVRv8A60h2JZWyBjrTYgI32t35601+W60hyGGBTEkWmhV8cc9CPWqrRFTgrkdweoq/EyMMHNLLEHTjIbsRQMxpIurKTU1nq9zYHCHcnXY/IpZ1aNiJBt569jVWRc44/KqU3HYOW51Vp4lsLjCz5gfOOeV/Othdk6B4XWRfVTkV5o6hG5B+tOju7m3IaCd09CrVtGsupDgz0zyyR1GKjMC7eRmuNtvF+oQ4E4SdR13DB/OteHxlZPxLbSxn2IIrRTi+orWNaVQFJ4wBWHokb3c93qZ48x9kef7op+p61Ff6dJDYMRLJ8mXGMDvWha3FnbWscCSoqRoF5ovfYCcgoM/oKcoLDNV31WyVTumUn0ANZt14mih/1MBf3c4ptpbiNzHPPNZuoanBbRsisHlHYHgVzl34iu7obSRGn91DismW5LN8ueeprGVVdClEvXd+0xwDkk8iktLV5fnfIHqaW009s+bMp9cVoySJFGccYGBWDdy1oRtIIYjEnTvUa7nIPt61Bv3kfzq3Em1QQenWkDRYXIQAt70xiSc55Hp3pSTtHP6dafGOO2KBEygCMEjB9KaWxl26jpS7lI6ZA44qOV8qAPrTDcIhlt3UjvT5MYJBII/WmoAgGDkmkkJPTuaBDoVCoTg5JqK5n8tdgPtSswSPHQ1SaTfJg8ntQPUngBZctmpoMeYTwW9KhJwoHap7YgKTjn1oBO4jMTIT+GDVhRgDbVZ8ebz+NWMbVBUfrQA4/OfmBqZTj5TUSFsklse9SckZ6n1oAf5oiiLDsCaqWepG5uGicKeMg9Khv7kLEI1GCepqLTdm4tgZFXB2ZLjdGsxP4d6QA56/LSkkA+p60zIPtXcjnvqaNi4e3kjH8JrEvo/LumGMDqK0rJ9k4U5w3FQazHyrhfYmtKbswZRj+5k9qcMGoYmzxU3A7/nXRuLqBx0zikOR0Jpc5AGKQ8kE5Ap6IAB4689qA3ODS8+g9qMepqQBWyev0oI/GnKARgH86acg4NMDvv8Ahpf/AKlL/wAqX/2qj/hpf/qUv/Kl/wDaq8Cor589E97P7Suf+ZS/8qP/ANqpp/aSJ/5lP/yo/wD2qvBqKAPdT+0cT/zKv/lQ/wDtVMP7RWf+ZW/8qH/2qvDaKAPcD+0Pn/mV/wDyof8A2uoz+0Hk/wDIsf8Ak/8A/a68TooA9qP7QBP/ADLP/k//APa6Yfj7n/mWv/J7/wC114xRQB7Kfj1n/mWv/J7/AO10w/Hcn/mXP/J7/wC1147RQB6DrPxO/te8+0HSDEdgXH2rd/7IKyn8aK//ADDQP+23/wBjXJ0UCsdO3i5GH/IPx/22/wDsahHikq2Raf8AkT/61c9RTuFkdA3ihmbcLXH/AG0/+tSf8JMf+fX/AMif/WrAopBZHQf8JN/058/9dP8A61V5NdLuHFvggY+//wDWrHooCyNltf3EH7NyP+mn/wBalXxAVbd9m/8AIn/1qxaKA5UbMmvCRs/Zcf8AbT/61WF8UBRj7EOmP9Z/9aueooDlRuHxFl8i1wPTzP8A61K/iMOuDacf9dP/AK1YVFFw5UbS+ICrK32bJH/TT/61OfxEHP8Ax6Y/7af/AFqw6KAsjYGvY/5duf8Af/8ArU7+3/8Ap2/8if8A1qxaKdxcqNr/AISD/p2/8if/AFqX/hIMYxa8D/pp/wDWrEoouPlRtHxBn/l1/wDIn/1qG18N/wAuuP8Atp/9asWii4cqN6PxKY1x9lz/ANtP/rVL/wAJUcY+x/8AkX/61c5RRcOVG83iRW62Wc9cy5H8qrHWQGJS3K56jzMj+VZVFK4WRq/21lcfZ/8Ax/j+VRtqaMf+PYD6P/8AWrOooCyLr34bpDj/AIF/9amfbP8AY/WqtFAWReTUnjPyrj/gVTrrsy/8swfqf/rVlUVXMxcqNZtckYEGL8d3/wBaqz6i79V/WqVFJybHyosG6JPK/rVq31OG3AP2Tc3qZP8A61ZtFILI3T4kYji2x/20/wDrVWfWmk6w8f7/AP8AWrLop3CyNSPWNmP9Hz/wP/61WP8AhIcDAtcf9tP/AK1YdFK4WRuf8JEcj/Ren/TT/wCtUn/CTDbj7Hz6+b/9aufop3Fyo6AeJgowLP8A8if/AFqafEmWB+yfh5n/ANasGii4cqOgPifOP9D5H/TX/wCtSN4lLMCbTp/00/8ArVgUUXYcqNyXxGZB/wAeuPfzP/rVFFrfltk224/7/wD9asiii4+VG0dfySfs3/kT/wCtUkfiXy1x9kyfXzP/AK1YNFHMxcqNw+IsnJtcn/rp/wDWqVPFLL960DD/AK6f/WrnqKLsOVHRHxVz/wAeQH/bX/61L/wlZxj7H/5F/wDrVzlFF2HKjYl10ytk2+P+B/8A1qmtvEYtwR9k3Z/6aY/pWDRRcfKjpz4vyP8Ajw/8jf8A2NN/4Swf8+P/AJF/+tXNUVoq9RdSPYw7HTDxcVdWFlyDn/W//WqS78Zi7jKnT9vp++z/AOy1ytFP29TuHsodjaHiDB/49v8AyJ/9apT4mBx/ofT/AKa//WrAoqvrVXv+Qeyh2N8eJsf8uf8A5F/+tS/8JP8A9Of/AJE/+tXP0UfWq3f8g9jDsb//AAk3/Tp/5E/+tSjxMAP+PPn183/61c/RR9aq9/yD2MOx0I8UYB/0Pn183/61J/wlBzk2mf8Atp/9aufoo+tVe/5B7GHYKKKK5zQKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigD//2Q==";
          Log.e("TAG", "PhotoData: "+image );
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

//    public void setBounding(int[][] bounding) {
//        this.bounding = bounding;
//    }
}