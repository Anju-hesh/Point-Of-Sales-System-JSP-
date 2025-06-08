function generateNewCustomerId() {
    $.ajax({
        url: "http://localhost:8080/pos_system_jsp_Web_exploded/customer-id",
        method: "GET",
        dataType: "json",
        success: function (data) {
            // console.log("Received ID:", data.customerId);
            $("#customerId").val(data.customerId);
        },
        error: function (xhr, status, error) {
            console.error("Error:", error);
            alert("Failed to generate customer ID");
        }
    });
}

// generateNewCustomerId();

$(document).ready(function () {
    generateNewCustomerId();
});

$("#getCustomers").on('click', function () {
    $.ajax({
        url: "http://localhost:8080/pos_system_jsp_Web_exploded/customer",
        method: "GET",
        dataType: "json",
        success: function (data) {
            displayEvents(data);
        },
        error: function (xhr, status, error) {
            console.error("Error fetching customers:", error);
            console.error("Status:", status);
            console.error("Response Text:", xhr.responseText);
            alert("Failed to load customers. Please try again later.");
        }
    });
});

function displayEvents(customers) {
    const tBody = $("#table-body");
    tBody.empty();

    if (customers.length === 0) {
        tBody.append('<tr><td colspan="6" style="text-align: center; color: #6c757d;">No events found</td></tr>');
        return;
    }

    customers.forEach(function (customer) {
        const row = $(`
            <tr>
               <td>${customer.id}</td>
               <td>${customer.name}</td>
               <td>${customer.address}</td> 
               <td>${customer.salary}</td>
               <td>
                  <button class="btn btn-warning" onclick="editCustomer('${customer.id}')">✏️ Edit</button>
                  <button class="btn btn-danger" onclick="deleteCustomer('${customer.id}')">🗑️ Delete</button>
               </td>
            </tr>   
        `);
        tBody.append(row);
    });
}

$("#saveButton").on('click', function () {
    let isValid = validateCustomerForm();
    if (!isValid) return;

    const isUpdateMode = $(this).data("mode") === "update";
    const customerId = isUpdateMode ? $(this).data("customerId") : $("#customerId").val();

    const customerData = {
        id: customerId,
        name: $("#customerName").val(),
        address: $("#customerAddress").val(),
        salary: $("#customerSalery").val(),
        action: isUpdateMode ? "update" : "save"
    };

    $.ajax({
        url: `http://localhost:8080/pos_system_jsp_Web_exploded/customer`,
        method: isUpdateMode ? "PUT" : "POST",
        data: customerData,
        success: function () {
            alert(isUpdateMode ? "Customer Updated Successfully!" : "Customer Saved Successfully!");
            $("#getCustomers").click();
            resetCustomerForm();
            $("#saveButton").text("Save").removeData("mode").removeData("customerId");
            updateCustomerDropdown();
        },
        error: function (xhr, status, error) {
            console.error("Error saving/updating customer:", error);
            alert("Failed to save/update customer. Please try again.");
        }
    });
});

function editCustomer(customerId) {
    $.ajax({
        url: `http://localhost:8080/pos_system_jsp_Web_exploded/customer?id=${customerId}`,
        method: "GET",
        dataType: "json",
        success: function (customer) {
            $("#customerId").val(customer.id);
            $("#customerName").val(customer.name);
            $("#customerAddress").val(customer.address);
            $("#customerSalery").val(customer.salary);

            $("#saveButton").text("Update").data("mode", "update").data("customerId", customerId);
        },
        error: function (xhr, status, error) {
            console.error("Error loading customer data:", error);
            alert("Failed to load customer data for editing.");
        }
    });
}

function validateCustomerForm() {
    let isValid = true;
    $(".error").text("");
    $("input").removeClass("error-border");

    function validateField(fieldId, errorId, errorMsg) {
        let value = $(fieldId).val();
        if (value === "") {
            $(errorId).text(errorMsg);
            $(fieldId).addClass("error-border");
            isValid = false;
        } else {
            $(errorId).text("");
            $(fieldId).removeClass("error-border");
        }
    }

    validateField("#customerId", "#customerIdError", "Customer ID is required");
    validateField("#customerName", "#customerNameError", "Customer Name is required");
    validateField("#customerAddress", "#customerAddressError", "Customer Address is required");
    validateField("#customerSalery", "#customerSaleryError", "Customer Salery is required");

    const salary = $("#customerSalery").val();
    if (salary !== "" && isNaN(salary)) {
        $("#customerSaleryError").text("Salary must be a number");
        $("#customerSalery").addClass("error-border");
        isValid = false;
    }

    return isValid;
}

function resetCustomerForm() {
    $("#customerId").val("");
    $("#customerName").val("");
    $("#customerAddress").val("");
    $("#customerSalery").val("");
    $(".error").text("");
    $("input").removeClass("error-border");
    generateNewCustomerId();
}

function deleteCustomer(customerId) {
    // console.log("Deleting customer with ID:", customerId);

    if (confirm("Are you sure you want to delete this customer?")) {
        $.ajax({
            url: `http://localhost:8080/pos_system_jsp_Web_exploded/customer?id=${customerId}`,
            method: "DELETE",
            success: function () {
                alert("Customer deleted successfully!");
                $("#getCustomers").click();
                resetCustomerForm();
            },
            error: function (xhr, status, error) {
                console.error("Error deleting customer:", error);
                alert("Failed to delete customer. Please try again.");
            }
        });
    }
}

function updateCustomerDropdown() {
    $.ajax({
        url: "/pos_system_jsp_Web_exploded/customer",
        method: "GET",
        dataType: "json",
        success: function(customers) {
            let dropdown = $("#orderCustomerId");
            dropdown.empty();
            dropdown.append(`<option value="">-- Select Customer --</option>`);
            customers.forEach(function(customer) {
                dropdown.append(`<option value="${customer.id}">${customer.id} - ${customer.name}</option>`);
            });
        },
        error: function() {
            alert("Failed to load customers");
        }
    });
}
