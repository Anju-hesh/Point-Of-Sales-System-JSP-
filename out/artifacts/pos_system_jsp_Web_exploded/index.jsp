<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>POS Management</title>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>

</head>
<body>

<!-- 🔹 Navigation Bar -->
<nav class="navbar">
    <a href="#customerSection">Customer</a>
    <a href="#itemSection">Item</a>
    <a href="#orderSection">Order</a>
    <button class="burger-button">☰</button>
</nav>


    <!-- 🔹 Customer Section -->
  <div class="container">
    <section id="customerSection">
      <div id="customer-section" class="form-section">
        <h2>Add New Customer</h2>
        <form method="post" action="${pageContext.request.contextPath}/customer" id="customerForm">
            <input type="hidden" name="action" value="save">
            <label for="customerId">ID:</label><br>
            <input type="text" id="customerId" name="id" required><br>
            <span class="error" id="customerIdError"></span><br>

            <label for="customerName">Name:</label><br>
            <input type="text" id="customerName" name="name" placeholder="Anjana Heshan" required><br>
            <span class="error" id="customerNameError"></span><br>

            <label for="customerAddress">Address:</label><br>
            <input type="text" name="address" id="customerAddress" placeholder="5/32 , Digala , Elpitiya." required><br>
            <span class="error" id="customerAddressError"></span><br>

            <label for="customerSalery">Salary:</label><br>
            <input type="text" name="salary" id="customerSalery" placeholder="00.00" required><br>
            <span class="error" id="customerSaleryError"></span><br>

            <button type="button" id="saveButton">Save</button>
            <button type="button" id="getCustomers">Get Customers</button>
        </form>
    </div>

    <div class="table-section">
        <h2>Customer List</h2>
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Address</th>
                <th>Salary</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody id="table-body"> </tbody>
        </table>
      </div>
    </section>
  </div>

  <div class="container">
    <!-- 🔹 Item Section -->
    <section id="itemSection">
      <div id="item-section" class="form-section">
        <h2>Add New Item</h2>
        <form method="post" action="${pageContext.request.contextPath}/item" id="itemForm">
            <input type="hidden" name="action" value="save">

            <label for="itemCode">Code:</label><br>
            <input type="text" id="itemCode" name="code" required><br>
            <span class="error" id="itemCodeError"></span><br>

            <label for="itemName">Name:</label><br>
            <input type="text" id="itemName" name="name" placeholder="Item Name" required><br>
            <span class="error" id="itemNameError"></span><br>

            <label for="itemPrice">Price:</label><br>
            <input type="text" name="price" id="itemPrice" placeholder="00.00" required><br>
            <span class="error" id="itemPriceError"></span><br>

            <label for="itemQtyOnHand">Quantity On Hand:</label><br>
            <input type="text" name="qty" id="itemQtyOnHand" placeholder="0" required><br>
            <span class="error" id="itemQtyOnHandError"></span><br>

            <button type="button" id="saveItemButton">Save Item</button>
            <button type="button" id="getItems">Get Items</button>
            <button type="button" id="updateItem">Update Items</button>
        </form>
    </div>

    <div class="table-section">
        <h2>Item List</h2>
        <table>
            <thead>
            <tr>
                <th>Code</th>
                <th>Name</th>
                <th>Price</th>
                <th>Qty</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody id="item-table-body"> </tbody>
        </table>
      </div>
    </section>
  </div>

<section id="orderSection">

    <div class="ordeContainer">

        <div class="invoice-container">
            <div class="section">
                <div class="section-header">
                    <h2>Invoice Details</h2>
                </div>
                <div class="section-content">
                    <div class="form-row">
                        <div class="form-group">
                            <label for="orderId">Order ID:</label>
                            <input type="text" id="orderId" name="orderId" class="form-control" required>
                            <span class="error" id="orderIdError"></span>
                        </div>
                        <div class="form-group">
                            <label for="orderDate">Date:</label>
                            <div class="date-input">
                                <input type="text" id="orderDate" name="orderDate" class="form-control" placeholder="mm/dd/yyyy" required>
                                <button type="button" id="calendarIcon" class="calendar-icon">📅</button>
                                <span class="error" id="dateError"></span>
                            </div>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label for="orderCustomerId">Customer:</label>
                            <select id="orderCustomerId" name="customerId" class="form-control" required>
                                <option value="">Select Customer</option>
                                <!-- Options populated dynamically -->
                            </select>
                            <span class="error" id="selectCustomerError"></span>
                        </div>
                        <div class="form-group">
                            <label for="orderCustomerName">Customer Name:</label>
                            <input type="text" id="orderCustomerName" class="form-control" readonly>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group full-width">
                            <label for="orderCustomerAddress">Address:</label>
                            <input type="text" id="orderCustomerAddress" class="form-control" readonly>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Item Select -->
            <div class="section">
                <div class="section-header">
                    <h2>Item Select</h2>
                </div>
                <div class="section-content">
                    <div class="form-row">
                        <div class="form-group">
                            <label for="orderItemCode">Item Code:</label>
                            <select id="orderItemCode" name="itemCode" class="form-control" required>
                                <option value="">Select Item Code</option>
                                <!-- Options populated dynamically -->
                            </select>
                            <span class="error" id="selectItemError"></span>
                        </div>
                        <div class="form-group">
                            <label for="orderItemName">Item Name:</label>
                            <input type="text" id="orderItemName" class="form-control" readonly>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label for="orderItemPrice">Price:</label>
                            <input type="text" id="orderItemPrice" class="form-control" readonly>
                        </div>
                        <div class="form-group">
                            <label for="orderQtyOnHand">Qty On Hand:</label>
                            <input type="text" id="orderQtyOnHand" class="form-control" readonly>
                        </div>
                        <div class="form-group">
                            <label for="orderQuantity">Order Quantity:</label>
                            <input type="number" id="orderQuantity" class="form-control" min="1" required>
                            <span class="error" id="orderQuantityError"></span>
                        </div>
                    </div>
                    <div class="form-row button-row">
                        <button id="addItemBtn" class="btn btn-danger" type="button">Add Item</button>
                        <button id="cancelEditBtn" class="btn btn-secondary d-none">Cancel Edit</button>
                    </div>
                </div>
            </div>

            <!-- Total and Purchase -->
            <div class="section total-section">
                <div class="total-content">
                    <h2 class="total">Total: <span id="totalAmount">0.00</span> Rs/=</h2>
                    <h3 class="subtotal">SubTotal: <span id="subtotalAmount">0.00</span> Rs/=</h3>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="cashAmount">Cash:</label>
                            <input type="number" id="cashAmount" class="form-control" min="0">
                            <span class="error" id="cashError"></span>
                        </div>
                        <div class="form-group">
                            <label for="discountPercentage">Discount (%):</label>
                            <input type="number" id="discountPercentage" class="form-control" min="0" max="100" step="1">
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group full-width">
                            <label for="balanceAmount">Balance:</label>
                            <input type="text" id="balanceAmount" class="form-control" readonly>
                            <span class="error" id="balanceError"></span>
                        </div>
                    </div>

                    <div class="form-row button-row">
                        <button id="purchaseBtn" class="btn btn-success" type="button">Purchase</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Order Table -->
        <div class="order-table-container">
            <table class="order-table">
                <thead>
                <tr>
                    <th>Item Code</th>
                    <th>Item Name</th>
                    <th>Price</th>
                    <th>Quantity</th>
                    <th>Total</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody id="orderTableBody">  </tbody>
            </table>
        </div>
    </div>
</section>

<script>
    function toggleSections() {
        const sections = {
            "#customerSection": document.getElementById("customerSection"),
            "#itemSection": document.getElementById("itemSection"),
            "#orderSection": document.getElementById("orderSection")
        };

        Object.values(sections).forEach(section => {
            if (section) section.style.display = "none";
        });

        const currentHash = window.location.hash || "#customerSection";
        if (sections[currentHash]) {
            sections[currentHash].style.display = "block";
        } else {
            sections["#customerSection"].style.display = "block";
        }
    }

    function handleNavClick(event) {
        event.preventDefault();
        const targetHash = event.currentTarget.getAttribute("href");
        if (targetHash) {
            window.location.hash = targetHash;
        }
    }

    window.addEventListener("DOMContentLoaded", () => {
        toggleSections();

        window.addEventListener("hashchange", toggleSections);

        document.querySelectorAll(".navbar a").forEach(link => {
            link.addEventListener("click", handleNavClick);
        });
    });
</script>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<script src="${pageContext.request.contextPath}/js/item.js"></script>
<script src="${pageContext.request.contextPath}/js/customer.js"></script>
<script src="${pageContext.request.contextPath}/js/order.js"></script>
</body>
</html>
