function deleteEmployee(employeeId) {
    if (confirm("Are you sure you want to delete this employee?")) {
        fetch( '/ayxkix/employee', {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ employeeId: employeeId })
        })
        .then(response => {
            if( response.ok) {
                // Reload the page to reflect changes
                window.location.reload();
            }
            else {
                throw new Error(response.statusText);
            }

        })
        .catch(error => {
            console.error('Error:', error);
            alert("An error occurred while deleting the employee.");
        });
    }
}