function employeeUtils(employeeId) {
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
        });
    }
}

function updateEmployee() {
    const form = document.getElementById('updateEmployeeForm');
    const formData = new FormData(form);
    const employeeData = Object.fromEntries(formData.entries());

    employeeData.employeeId = document.getElementById('editEmployeeId').value;

    fetch('/ayxkix/employee', {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(employeeData)
    })
        .then(response => {
            if (response.ok) {
                window.location.reload();
            } else {
                throw new Error(response.statusText);
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}
