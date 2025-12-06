
    /********* Estado del carrito (persistido) *********/
    let cart = JSON.parse(localStorage.getItem('mambo_cart_v1') || '[]');

    // Elementos UI
    const productsGrid = document.getElementById('productsGrid');
    const cartItemsArea = document.getElementById('cartItemsArea');
    const cartTotalEl = document.getElementById('cartTotal');
    const cartBadge = document.getElementById('cartBadge');
    
    /********* Render de productos *********/
    /********* Render de productos *********/




      // añadir listeners botones agregar
      document.addEventListener('DOMContentLoaded', () => {
  // cuando la página se carga, añade los listeners a los botones generados por Thymeleaf
  document.querySelectorAll('.add-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      const id = btn.dataset.id;
      const nombre = btn.dataset.nombre;
      const precio = parseFloat(btn.dataset.precio);
      const img = btn.dataset.img;

      const product = { id, name: nombre, price: precio, img };
      addToCart(product);
    });
  });

  updateCartUI(); // 
});


  
    /********* Funciones carrito *********/
    function saveCart() { localStorage.setItem('mambo_cart_v1', JSON.stringify(cart)); }
    function getCartCount() { return cart.reduce((s, i) => s + i.qty, 0); }

    function addToCart(product) {
      const existing = cart.find(i => i.id === product.id);
      if (existing) existing.qty += 1;
      else cart.push({ id: product.id, name: product.name, price: product.price, img: product.img, qty: 1 });
      saveCart(); updateCartUI();
      // abrir offcanvas visual (opcional)
      const offcanvasEl = document.getElementById('cartCanvas');
      const bs = bootstrap.Offcanvas.getOrCreateInstance(offcanvasEl);
      bs.show();
    }

    function removeFromCart(id) {
      cart = cart.filter(i => i.id !== id);
      saveCart(); updateCartUI();
    }

    function changeQty(id, delta) {
      const it = cart.find(i => i.id === id);
      if (!it) return;
      it.qty += delta;
      if (it.qty < 1) it.qty = 1;
      saveCart(); updateCartUI();
    }

    function computeTotal() {
      return cart.reduce((s, i) => s + i.price * i.qty, 0);
    }

    function updateCartUI() {
      // badge
      const totalCount = getCartCount();
      cartBadge.textContent = totalCount;
      // items
      cartItemsArea.innerHTML = '';
      if (cart.length === 0) {
        cartItemsArea.innerHTML = '<p class="muted">No hay productos en el carrito.</p>';
      } else {
        cart.forEach(item => {
          const div = document.createElement('div');
          div.className = 'cart-item';
          div.innerHTML = `
            <img src="${item.img}" alt="${item.name}" />
            <div class="cart-info">
              <h6>${item.name}</h6>
              <p>Unit: S/ ${item.price.toFixed(2)}</p>
              <div class="qty-control">
                <button class="btn-decrease" data-id="${item.id}">−</button>
                <span style="min-width:24px;display:inline-block;text-align:center">${item.qty}</span>
                <button class="btn-increase" data-id="${item.id}">+</button>
              </div>
            </div>
            <div style="text-align:right;">
              <div class="subtotal">S/ ${(item.price * item.qty).toFixed(2)}</div>
              <div style="margin-top:.6rem;">
                <button class="remove-btn" data-id="${item.id}">Eliminar</button>
              </div>
            </div>
          `;
          cartItemsArea.appendChild(div);
        });

        // listeners
        cartItemsArea.querySelectorAll('.btn-decrease').forEach(b => b.addEventListener('click', e => {
          changeQty(e.currentTarget.dataset.id, -1);
        }));
        cartItemsArea.querySelectorAll('.btn-increase').forEach(b => b.addEventListener('click', e => {
          changeQty(e.currentTarget.dataset.id, +1);
        }));
        cartItemsArea.querySelectorAll('.remove-btn').forEach(b => b.addEventListener('click', e => {
          removeFromCart(e.currentTarget.dataset.id);
        }));
      }
      // total
      cartTotalEl.textContent = `S/ ${computeTotal().toFixed(2)}`;
    }

    /********* FILTRADO / BÚSQUEDA *********/
    function applyFilters() {
      const q = document.getElementById('searchInput').value.trim().toLowerCase();
      const minP = parseFloat(document.getElementById('minPrice').value) || 0;
      const maxP = parseFloat(document.getElementById('maxPrice').value) || Infinity;
      // categories selected by sidebar buttons? we detect active class
      const activeCatBtn = [...document.querySelectorAll('.category-btn')].find(b => b.classList.contains('active'));
      const cat = activeCatBtn ? activeCatBtn.dataset.cat : null;

     
    }

    function clearFilters() {
      document.getElementById('searchInput').value = '';
      document.getElementById('minPrice').value = '';
      document.getElementById('maxPrice').value = '';
      document.querySelectorAll('.category-btn').forEach(b => b.classList.remove('active'));
      
    }

    /********* INIT *********/
    document.addEventListener('DOMContentLoaded', () => {
      
      updateCartUI();

      // sidebar category buttons (toggle active)
      document.querySelectorAll('.category-btn').forEach(btn => {
        btn.addEventListener('click', () => {
          // toggle active marker
          const active = btn.classList.contains('active');
          document.querySelectorAll('.category-btn').forEach(b => b.classList.remove('active'));
          if (!active) btn.classList.add('active');
          applyFilters();
        });
      });

      // chips below
      document.querySelectorAll('.chip').forEach(ch => {
        ch.addEventListener('click', () => {
          const cat = ch.dataset.cat;
          document.querySelectorAll('.category-btn').forEach(b => b.classList.remove('active'));
          const target = [...document.querySelectorAll('.category-btn')].find(b => b.dataset.cat === cat);
          if (target) target.classList.add('active');
          applyFilters();
          // scroll to products
          window.scrollTo({ top: document.querySelector('.catalog-top').offsetTop - 60, behavior: 'smooth' });
        });
      });

      // search and price filter
      document.getElementById('searchBtn').addEventListener('click', applyFilters);
      document.getElementById('searchInput').addEventListener('keydown', e => { if (e.key === 'Enter') applyFilters(); });
      document.getElementById('applyFilter').addEventListener('click', applyFilters);
      document.getElementById('clearFilter').addEventListener('click', clearFilters);

      // checkout
      document.getElementById('checkoutBtn').addEventListener('click', () => {
        if (cart.length === 0) {
          alert('Tu carrito está vacío.');
          return;
        }
        alert('Simulación de compra — total: ' + computeTotal().toFixed(2));
      });

      // simple login handler (demo)
      document.getElementById('loginForm')?.addEventListener('submit', e => {
        e.preventDefault();
        const bs = bootstrap.Modal.getInstance(document.getElementById('loginModal'));
        bs.hide();
        alert('Sesión iniciada (demo)');
      });
    });
    
