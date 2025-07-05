function initChart() {
                    const canvas = document.getElementById('saleChart');
                    if (!canvas) {
                        console.error('Chart canvas not found');
                        return;
                    }

                    const ctx = canvas.getContext('2d');
    const config = {
        type: 'bar',
        data: data,
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top',
                },
                title: {
                    display: true,
                    text: 'Order Statistics by Month',
                }
            }
        },
    };

                    new Chart(ctx, config);
                }

function initDounutChart() {
    const canvas = document.getElementById('myDoughnutChart');
    if (!canvas) {
        console.error('Doughnut chart canvas not found');
        return;
    }

    const ctx = canvas.getContext('2d');

    const DoughnutConfig = {
        type: 'doughnut',
        data: dataDoughnut,
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top',
                },
                title: {
                    display: true,
                    text: 'Trending Order Statistics',
                }
            }
        },
    };

    new Chart(ctx, DoughnutConfig);
}