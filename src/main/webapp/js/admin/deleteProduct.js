function deleteProduct(productId) {
    fetch('/ayxkix/product', {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ productId: productId })
    })
        .then(response => {
           window.location.reload(); // Reload the page to reflect changes
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Có lỗi xảy ra khi xóa sản phẩm.');
        });
}
