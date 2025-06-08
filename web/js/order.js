$(document).ready(function () {
    generateNewOrderCode();
    loadCustomerIDs();
    loadItemIDs();
    var fp = flatpickr("#orderDate", {
        dateFormat: "m/d/Y",
        allowInput: true
    });

    $('#calendarIcon').on('click', function () {
        fp.open();
    });

    calculateSubTotalAndBalance();
});

function generateNewOrderCode() {
    $.ajax({
        url: "http://localhost:8080/pos_system_jsp_Web_exploded/order-id",
        method: "GET",
        dataType: "json",

        success: function (data) {
            console.log("Received Code:", data.orderId);
            $("#orderId").val(data.orderId);
        },
        error: function (xhr, status, error) {
            console.error("Error:", error);
            alert("Failed to generate order code");
        }
    });

    $("#orderCustomerId").on("change", function () {
        const selectedId = $(this).val();

        if (selectedId) {
            $.ajax({
                url: `http://localhost:8080/pos_system_jsp_Web_exploded/customer?id=${selectedId}`,
                method: "GET",
                dataType: "json",
                success: function (customer) {
                    $("#orderCustomerName").val(customer.name);
                    $("#orderCustomerAddress").val(customer.address);
                },
                error: function (xhr, status, error) {
                    console.error("Error fetching customer details:", error);
                    alert("Failed to load customer details.");
                }
            });
        } else {
            $("#orderCustomerName").val("");
            $("#orderCustomerAddress").val("");
        }
    });

    $("#orderItemCode").on("change", function () {
        const selectedCode = $(this).val();

        if (selectedCode) {
            console.log("Selected Item Code:", selectedCode);
            $.ajax({
                url: `http://localhost:8080/pos_system_jsp_Web_exploded/item?code=${selectedCode}`,
                method: "GET",
                dataType: "json",
                success: function (item) {
                    $("#orderItemName").val(item.name);
                    $("#orderItemPrice").val(item.price);
                    $("#orderQtyOnHand").val(item.qtyOnHand)
                },
                error: function (xhr, status, error) {
                    console.error("Error fetching item details:", error);
                    alert("Failed to load item details.");
                }
            });
        } else {
            $("#orderItemName").val("");
            $("#orderItemPrice").val("");
            $("#orderQtyOnHand").val("");
        }
    });
}

function loadCustomerIDs() {
    $.ajax({
        url: "http://localhost:8080/pos_system_jsp_Web_exploded/customer",
        method: "GET",
        dataType: "json",
        success: function (data) {
            console.log("Loaded customers:", data);

            const $dropdown = $("#orderCustomerId");
            $dropdown.empty();
            $dropdown.append(`<option value="">-- Select Customer ID --</option>`);

            data.forEach(function (customer) {
                $dropdown.append(
                    `<option value="${customer.id}">${customer.id} - ${customer.name}</option>`
                );
            });
        },
        error: function (xhr, status, error) {
            console.error("Error loading customer IDs:", error);
            alert("Failed to load customer IDs.");
        }
    });
}

function loadItemIDs() {
    $.ajax({
        url: "http://localhost:8080/pos_system_jsp_Web_exploded/item",
        method: "GET",
        dataType: "json",
        success: function (data) {
            console.log("Loaded items:", data);

            const dropdown = $("#orderItemCode");
            dropdown.empty();
            dropdown.append(`<option value="">-- Select Item ID --</option>`);

            data.forEach(function (item) {
                dropdown.append(
                    `<option value="${item.code}">${item.code} - ${item.name}</option>`
                );
            });
        },
        error: function (xhr, status, error) {
            console.error("Error loading item IDs:", error);
            alert("Failed to load item IDs.");
        }
    });
}

let orderItems = [];
let editingIndex = -1;

$("#addItemBtn").on("click", function () {
    // addItemToOrder();

    const code = $("#orderItemCode").val();
    const name = $("#orderItemName").val();
    const price = parseFloat($("#orderItemPrice").val());
    const qty = parseInt($("#orderQuantity").val());
    const qtyOnHand = parseInt($("#orderQtyOnHand").val());

    if (!code || !name || isNaN(price) || isNaN(qty)) {
        alert("Please fill all item details correctly.");
        return;
    }

    if (qty > qtyOnHand) {
        alert("Not enough quantity in stock!");
        return;
    }

    const total = price * qty;

    if (editingIndex === -1) {

        const existingItem = orderItems.find(item => item.code === code);
        if (existingItem) {
            existingItem.qty += qty;
            existingItem.total += total;
        } else {
            orderItems.push({ code, name, price, qty, total });
        }
    } else {

        orderItems[editingIndex] = { code, name, price, qty, total };
        editingIndex = -1;

        $("#addItemBtn")
            .text("Add Item")
            .removeClass("btn-warning")
            .addClass("btn-primary");
        $("#orderItemCode").prop("disabled", false);
    }

    updateOrderTable();
    updateTotalAmount();
    clearItemFields();
});

function updateOrderTable() {
    const $tbody = $("#orderTableBody");
    $tbody.empty();

    orderItems.forEach((item, index) => {
        const row = $(`
            <tr data-index="${index}">
                <td>${item.code}</td>
                <td>${item.name}</td>
                <td>${item.price.toFixed(2)}</td>
                <td>${item.qty}</td>
                <td>${item.total.toFixed(2)}</td>
                <td>
                    <button class="btn btn-danger btn-sm" onclick="removeItem(${index})">Remove</button>
                </td>
            </tr>
        `);

        row.on("click", function () {
            editingIndex = index;

            $("#orderItemCode").val(item.code).prop("disabled", true).trigger("change");
            $("#orderItemName").val(item.name);
            $("#orderItemPrice").val(item.price.toFixed(2));
            $("#orderQuantity").val(item.qty);
            $("#orderQtyOnHand").val("");

            $("#addItemBtn")
                .text("Update Item")
                .removeClass("btn-primary")
                .addClass("btn-warning");
        });

        $tbody.append(row);
    });
}

$("#orderQuantity").on("input", function () {
    const qty = parseInt($("#orderQuantity").val());
    console.log("Quantity entered:", qty);

    const itemCode = $("#orderItemCode").val();

    if (!itemCode || isNaN(qty)) return;

    $.ajax({
        url: "http://localhost:8080/pos_system_jsp_Web_exploded/item",
        method: "GET",
        data: { code: itemCode },
        success: function (res) {
            const quantityOnHand = res.qtyOnHand;
            console.log("Quantity on hand for item " + itemCode + ": " + quantityOnHand);

            if (qty > quantityOnHand) {
                $("#orderQuantity").addClass("is-invalid");
                $("#orderQuantityError").text("Not enough quantity in stock! , You Can't Add To Cart");
                $("#addItemBtn").prop("disabled", true);
                $("#purchaseBtn").prop("disabled", true);
            } else {
                $("#orderQuantity").removeClass("is-invalid");
                $("#orderQuantityError").text("");
                $("#addItemBtn").prop("disabled", false);
                $("#purchaseBtn").prop("disabled", false);
            }
        },
        error: function () {
            console.error("Failed to fetch item quantity from server.");
        }
    });
});


function updateTotalAmount() {
    const totalAmount = orderItems.reduce((sum, item) => sum + item.total, 0);
    $("#totalAmount").text(totalAmount.toFixed(2));

    calculateSubTotalAndBalance();
}

function clearItemFields() {
    $("#orderItemCode").val("");
    $("#orderItemName").val("");
    $("#orderItemPrice").val("");
    $("#orderQuantity").val("");
    $("#orderQtyOnHand").val("");

    editingIndex = -1;
    $("#addItemBtn").text("Add Item").removeClass("btn-warning").addClass("btn-primary");
}

$("#cancelEditBtn").on("click", clearItemFields);

function clearItemFields() {
    $("#orderItemCode").val("").prop("disabled", false);
    $("#orderItemName").val("");
    $("#orderItemPrice").val("");
    $("#orderQuantity").val("");
    $("#orderQtyOnHand").val("");

    $("#orderQuantity").removeClass("is-invalid");
    $("#orderQuantityError").text("");
    $("#addItemBtn").prop("disabled", false);
    $("#purchaseBtn").prop("disabled", false);

    editingIndex = -1;

    $("#addItemBtn").text("Add Item").removeClass("btn-warning").addClass("btn-primary");
    $("#cancelEditBtn").addClass("d-none");
}

$("#cashAmount, #discountPercentage").on("input", function () {
    calculateSubTotalAndBalance();
});

function calculateSubTotalAndBalance() {
    const total = orderItems.reduce((sum, item) => sum + item.total, 0);
    let discount = parseFloat($("#discountPercentage").val()) || 0;
    let cash = parseFloat($("#cashAmount").val()) || 0;

    if (discount < 0) discount = 0;
    if (discount > 100) discount = 100;

    const discountAmount = (total * discount) / 100;
    const subtotal = total - discountAmount;
    let balance = cash - subtotal;

    $("#totalAmount").text(total.toFixed(2));
    $("#subtotalAmount").text(subtotal.toFixed(2));

    $("#balanceAmount").val(balance.toFixed(2));

    if (balance < 0) {
        $("#balanceError").text(" Cash is not enough!");
        $("#cashAmount").addClass("is-invalid");
    } else {
        $("#balanceError").text("");
        $("#cashAmount").removeClass("is-invalid");
    }

    if (discount > 100) {
        $("#cashError").text(" Discount cannot exceed 100%");
    } else {
        $("#cashError").text("");
    }
}

$("#purchaseBtn").on("click", function () {

    const orderId = $("#orderId").val();
    const customerId = $("#orderCustomerId").val();
    const orderDate = $("#orderDate").val();

    if (!orderId) {
        alert("Please enter an Order ID.");
        $("#orderId").focus();
        return;
    }

    if (!customerId) {
        alert("Please enter a Customer ID.");
        $("#orderCustomerId").focus();
        return;
    }

    if (!orderDate) {
        alert("Please select an Order Date.");
        $("#orderDate").focus();
        return;
    }

    if (!orderItems || orderItems.length === 0) {
        alert("Please add at least one item to the order.");
        return;
    }

    const invalidItems = orderItems.filter(item => !item.code || !item.qty || item.qty <= 0);
    if (invalidItems.length > 0) {
        alert("Please ensure all items have valid codes and quantities greater than 0.");
        return;
    }

    const orderData = {
        orderId: orderId,
        customerId: customerId,
        orderDate: orderDate,
        items: orderItems.map(item => ({
            code: item.code,
            qty: parseInt(item.qty)
        }))
    };

    const $btn = $("#purchaseBtn");
    const originalText = $btn.text();
    $btn.prop("disabled", true).text("Processing...");

    $.ajax({
        url: "purchase-order",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify(orderData),
        timeout: 30000,
        success: function (response) {
            alert("Order placed successfully!");
            clearOrderForm();

            if (response && response.orderNumber) {
                console.log("Order Number: " + response.orderNumber);
            }
        },
        error: function (xhr, status, error) {
            let errorMessage = "Failed to place order!";

            if (xhr.status === 400) {
                errorMessage = "Invalid order data. Please check your inputs.";
            } else if (xhr.status === 401) {
                errorMessage = "You are not authorized to place orders.";
            } else if (xhr.status === 500) {
                errorMessage = "Server error. Please try again later.";
            } else if (status === "timeout") {
                errorMessage = "Request timed out. Please try again.";
            } else if (xhr.responseJSON && xhr.responseJSON.message) {
                errorMessage = xhr.responseJSON.message;
            }

            alert(errorMessage);
            console.error("Order submission error:", error);
        },
        complete: function () {
            $btn.prop("disabled", false).text(originalText);
        }
    });
});

function clearOrderForm() {
    generateNewOrderCode();
    clearItemFields();
    $("#orderCustomerId").val("");
    $("#orderCustomerName").val("");
    $("#orderCustomerAddress").val("");
    orderItems = [];

    const $tbody = $("#orderTableBody");
    $tbody.empty();

    if (typeof refreshOrderItemsDisplay === 'function') {
        refreshOrderItemsDisplay();
    }

    // $(".order-form input, .order-form select, .order-form textarea").val("");

    $("#orderId").focus();
}

function confirmLargeOrder(totalItems) {
    if (totalItems > 10) {
        return confirm(`You are about to place an order with ${totalItems} items. Continue?`);
    }
    return true;
}

$("#purchaseBtnWithConfirm").on("click", function () {

    const totalItems = orderItems.reduce((sum, item) => sum + parseInt(item.qty), 0);
    if (!confirmLargeOrder(totalItems)) {
        return;
    }
});

function removeItem(index) {
    if (index >= 0 && index < orderItems.length) {
        orderItems.splice(index, 1);
        updateOrderTable();
        updateTotalAmount();
    } else {
        console.error("Invalid index for removing item:", index);
    }
}

$("#orderId").on("keypress", function (e) {
    if (e.which === 13) {
        const orderId = $("#orderId").val();
        console.log("Order ID entered:", orderId);

        if (!orderId) {
            alert("Please enter an order ID.");
            return;
        }

        orderItems = [];
        updateOrderTable();
        updateTotalAmount();

        $.ajax({
            url: `http://localhost:8080/pos_system_jsp_Web_exploded/order?orderId=${orderId}`,
            method: "GET",
            dataType: "json",
            success: function (order) {
                console.log("Order fetched:", order);

                $("#orderCustomerId").val(order.customerId).trigger("change");
                $("#orderCustomerName").val(order.customerId.name);
                $("#orderCustomerAddress").val(order.customerId.address);
                $("#orderDate").val(order.orderDate);

                orderItems = order.items.map(item => ({
                    code: item.code,
                    name: item.name,
                    price: item.price,
                    qty: item.qtyOnHand,
                    total: item.price * item.qtyOnHand
                }));

                updateOrderTable();
                updateTotalAmount();
            },
            error: function (xhr, status, error) {
                console.error("Failed to fetch order:", error);
                alert("Order not found or error occurred.");
            }
        });
    }
});
