angular.module('JWTDemoApp')

.controller('PDFController', function( $scope) {
    $scope.exportToPDF = function () {
        html2canvas(document.getElementById('exportToPDFThis'), {
            onrendered: function (canvas) {
                var data = canvas.toDataURL();
                var docDefinition = {
                    content: [{
                        image: data,
                        width: 500,
                    }]
                };
                pdfMake.createPdf(docDefinition).download("page.pdf");
            }
        });
    }
});