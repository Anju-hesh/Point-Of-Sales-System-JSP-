$(document).ready(function(){
    generateNewItemCode();
});

function generateNewItemCode(){
    $.ajax({
        url:"http://localhost:8080/pos_system_jsp_Web_exploded/item-code",
        method: "GET",
        dataType: "json",

        success: function (data){
            console.log("Received Code:", data.itemCode);
            $("#itemCode").val(data.itemCode);
        },
        error: function (xhr, status, error) {
            console.error("Error:", error);
            alert("Failed to generate item code");
        }
    });
}

$("#saveItemButton").on('click', function (){
    console.log("Save Item Button Clicked");

    const itemCode = $("#itemCode").val();
    const itemName = $("#itemName").val();
    const itemPrice = $("#itemPrice").val();
    const itemQty = $("#itemQtyOnHand").val();

    console.log("Item Code:", itemCode + " | Item Name:", itemName + " | Item Price:", itemPrice + " | Item Qty:", itemQty);

    if (!itemCode || !itemName || !itemPrice || !itemQty) {
        alert("Please fill in all fields.");
        return;
    }

    const itemData = {
        code: itemCode,
        name: itemName,
        price: parseFloat(itemPrice),
        qty: parseInt(itemQty)
    };

    // console.log("Item Data to be sent:", itemData);

    $.ajax({
        url: "http://localhost:8080/pos_system_jsp_Web_exploded/item",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify(itemData),
        success: function (response) {
            alert("Item saved successfully!");
            generateNewItemCode();
            $("#getItems").click();
            $("#itemName").val('');
            $("#itemPrice").val('');
            $("#itemQtyOnHand").val('');
            updateItemDropdown();
        },
        error: function (xhr, status, error) {
            console.error("Error saving item:", error);
            alert("Failed to save item. Please try again later.");
        }
    });
});

$("#getItems").on('click',function (){
    console.log("Get Items Button Clicked");

    $.ajax({
        url: "http://localhost:8080/pos_system_jsp_Web_exploded/item",
        method: "GET",
        dataType: "json",
        success: function (items) {
            console.log("Items received:", items);
            displayItems(items);
        },
        error: function (xhr, status, error) {
            console.error("Error fetching items:", error);
            alert("Failed to fetch items. Please try again later.");
        }
    });
})

function displayItems(items){
    console.log("Displaying Items:", items);
    const tBody = $("#item-table-body");
    tBody.empty();

    items.forEach(item => {
        const row = $(`
            <tr>
                <td>${item.code}</td>
                <td>${item.name}</td>
                <td>${item.price}</td>
                <td>${item.qtyOnHand}</td>
                <td>
                    <button class="btn btn-primary" onclick="editItem('${item.code}')">Edit</button>
                    <button class="btn btn-danger" onclick="deleteItem('${item.code}')">Delete</button>
                </td>
            </tr>
        `);
        tBody.append(row);
    });
}

function editItem(itemCode) {
    console.log("Edit Item Button Clicked for Item Code:", itemCode);

    $.ajax({
        url: `http://localhost:8080/pos_system_jsp_Web_exploded/item?code=${itemCode}`,
        method: "GET",
        dataType: "json",
        success: function (item) {
            console.log("Item to edit:", item);
            $("#itemCode").val(item.code);
            $("#itemName").val(item.name);
            $("#itemPrice").val(item.price);
            $("#itemQtyOnHand").val(item.qtyOnHand);
        },
        error: function (xhr, status, error) {
            console.error("Error fetching item for edit:", error);
            alert("Failed to fetch item details for editing.");
        }
    });
}

$("#updateItem").on('click',function (){
    console.log("Update Item Button Clicked");

    const itemCode = $("#itemCode").val();
    const itemName = $("#itemName").val();
    const itemPrice = $("#itemPrice").val();
    const itemQty = $("#itemQtyOnHand").val();

    console.log("Item Code:", itemCode + " | Item Name:", itemName + " | Item Price:", itemPrice + " | Item Qty:", itemQty);

    if (!itemCode || !itemName || !itemPrice || !itemQty) {
        alert("Please fill in all fields.");
        return;
    }

    const itemData = {
        code: itemCode,
        name: itemName,
        price: parseFloat(itemPrice),
        qty: parseInt(itemQty)
    };

    $.ajax({
        url: `http://localhost:8080/pos_system_jsp_Web_exploded/item?code=${itemCode}`,
        method: "PUT",
        contentType: "application/json",
        data: JSON.stringify(itemData),
        success: function (response) {
            alert("Item updated successfully!");
            generateNewItemCode();
            $("#getItems").click();
            $("#itemName").val('');
            $("#itemPrice").val('');
            $("#itemQtyOnHand").val('');
        },
        error: function (xhr, status, error) {
            console.error("Error updating item:", error);
            alert("Failed to update item. Please try again later.");
        }
    });
});

function deleteItem(itemCode) {
    console.log("Delete Item Button Clicked for Item Code:", itemCode);

    if (!confirm("Are you sure you want to delete this item?")) {
        return;
    }

    $.ajax({
        url: `http://localhost:8080/pos_system_jsp_Web_exploded/item?code=${itemCode}`,
        method: "DELETE",
        success: function (response) {
            alert("Item deleted successfully!");
            $("#getItems").click();
        },
        error: function (xhr, status, error) {
            console.error("Error deleting item:", error);
            alert("Failed to delete item. Please try again later.");
        }
    });
}

function updateItemDropdown() {
    $.ajax({
        url: "http://localhost:8080/pos_system_jsp_Web_exploded/item",
        method: "GET",
        dataType: "json",
        success: function(items) {
            let dropdown = $("#orderItemCode");
            dropdown.empty();
            dropdown.append(`<option value="">-- Select Item --</option>`);
            items.forEach(function(item) {
                dropdown.append(`<option value="${item.code}">${item.code} - ${item.name}</option>`);
            });
        },
        error: function() {
            alert("Failed to load items");
        }
    });
}