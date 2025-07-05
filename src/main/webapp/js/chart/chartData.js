let data = {
    labels: [],
    datasets: [
        {
            label: 'Order',
            data: [],
            backgroundColor: 'rgba(54, 162, 235, 0.7)',
        },
        {
            label: 'OrderGuest',
            data: [],
            backgroundColor: 'rgba(255, 99, 132, 0.7)',
        }
    ]
};


let dataDoughnut = {
    labels: [],
    datasets: [
        {
            label: '',
            data: [],
            backgroundColor: [
                'rgba(255, 99, 132, 0.7)',
                'rgba(54, 162, 235, 0.7)',
                'rgba(255, 206, 86, 0.7)',
                'rgba(75, 192, 192, 0.7)',
                'rgba(153, 102, 255, 0.7)',
                'rgba(255, 159, 64, 0.7)'
            ],
        }
    ]
};


async function loadOrderData() {
    try {
        const response = await fetch('order', {
            method: 'GET',
            headers: {'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
            }

        });

        if (!response.ok) throw new Error('Network response was not ok');

        const orderData = await response.json();
        // Debug: xem dữ liệu nhận được
        console.log('Received data:', orderData);

        // Tập hợp tất cả các tháng xuất hiện trong cả 2 bảng
        const monthsSet = new Set();
        orderData.order.forEach(o => monthsSet.add(o.month));
        orderData.orderGuest.forEach(o => monthsSet.add(o.month));

        // Sắp xếp và format nhãn tháng
        data.labels = Array.from(monthsSet).sort((a, b) => a - b)
            .map(month => `${month.toString().padStart(2, '0')}`);

        data.datasets[0].data = data.labels.map(label => {
            const month = parseInt(label);
            const order = orderData.order.find(o => o.month === month);
            return order ? order.total_money : 0;
        });
        data.datasets[1].data = data.labels.map(label => {
            const month = parseInt(label);
            const orderGuest = orderData.orderGuest.find(o => o.month === month);
            return orderGuest ? orderGuest.total_money : 0;
        });
        data.datasets[0].borderRadius = 12;
        data.datasets[1].borderRadius = 12;

        if (typeof initChart === 'function') {
            initChart();
        } else
            console.error('initChart function is not defined');


        // Xử lý dữ liệu cho biểu đồ Doughnut
        const trendingProducts = orderData.trendingProducts;
        if (trendingProducts && trendingProducts.length > 0) {
            dataDoughnut.labels = trendingProducts.map(p => p.product_type);
            dataDoughnut.datasets[0].data = trendingProducts.map(p => p.order_count);
            console.log(dataDoughnut.labels);
            console.log(dataDoughnut.datasets[0].data);
        } else {
            console.warn('No trending products data available');
        }
        if (typeof initDounutChart === 'function') {
            initDounutChart();
        } else {
            console.error('initDounutChart function is not defined');
        }

    } catch (error) {
        console.error('Error loading order data:', error);
    }
}

loadOrderData();
