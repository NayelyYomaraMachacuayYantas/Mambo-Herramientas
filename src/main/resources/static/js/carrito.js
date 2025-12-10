// Estado inicial del carrito
let cart = JSON.parse(localStorage.getItem('mambo_cart_v1') || '[]');

// UI Elements
const cartItemsArea = document.getElementById('cartItemsArea');
const cartTotalEl = document.getElementById('cartTotal');
const cartBadge = document.getElementById('cartBadge');

// Guardar carrito
function saveCart() { localStorage.setItem('mambo_cart_v1', JSON.stringify(cart)); }

// Contar productos
function getCartCount() { return cart.reduce((s,i)=>s+(i.qty||0),0); }

// Agregar producto
function addToCart(product) {
  const existing = cart.find(i=>i.id===product.id);
  if(existing) existing.qty += 1;
  else cart.push({...product, qty:1});
  saveCart(); updateCartUI();
  bootstrap.Offcanvas.getOrCreateInstance(document.getElementById('cartCanvas')).show();
}

// Eliminar producto
function removeFromCart(id) {
  cart = cart.filter(i=>i.id!==id);
  saveCart(); updateCartUI();
}

// Cambiar cantidad
function changeQty(id, delta) {
  const it = cart.find(i=>i.id===id); if(!it) return;
  it.qty += delta; if(it.qty<1) it.qty=1;
  saveCart(); updateCartUI();
}

// Total
function computeTotal(){ return cart.reduce((s,i)=>s+i.price*i.qty,0); }

// Actualizar UI
function updateCartUI() {
  if(cartBadge) cartBadge.textContent = getCartCount();
  cartItemsArea.innerHTML = '';
  if(cart.length===0){ cartItemsArea.innerHTML='<p>No hay productos en el carrito.</p>'; return; }

  cart.forEach(item=>{
    const div = document.createElement('div');
    div.className='cart-item';
    div.innerHTML=`
      <img src="${item.img}" alt="${item.name}" />
      <div>
        <h6>${item.name}</h6>
        <p>Unit: S/ ${item.price.toFixed(2)}</p>
        <div>
          <button class="btn-decrease" data-id="${item.id}">−</button>
          <span>${item.qty}</span>
          <button class="btn-increase" data-id="${item.id}">+</button>
        </div>
      </div>
      <div>S/ ${(item.price*item.qty).toFixed(2)}
        <button class="remove-btn" data-id="${item.id}">Eliminar</button>
      </div>
    `;
    cartItemsArea.appendChild(div);
  });

  cartItemsArea.querySelectorAll('.btn-decrease').forEach(b=>b.addEventListener('click',e=>changeQty(e.currentTarget.dataset.id,-1)));
  cartItemsArea.querySelectorAll('.btn-increase').forEach(b=>b.addEventListener('click',e=>changeQty(e.currentTarget.dataset.id,+1)));
  cartItemsArea.querySelectorAll('.remove-btn').forEach(b=>b.addEventListener('click',e=>removeFromCart(e.currentTarget.dataset.id)));

  cartTotalEl.textContent = computeTotal().toFixed(2);
}

// Delegación para botones "Agregar"
document.addEventListener('click', e=>{
  const addBtn = e.target.closest('.add-btn');
  if(addBtn){
    const product = {
      id: addBtn.dataset.id,
      name: addBtn.dataset.nombre,
      price: parseFloat(addBtn.dataset.precio),
      img: addBtn.dataset.img
    };
    addToCart(product);
  }
});

// Inicializar UI al cargar
document.addEventListener('DOMContentLoaded', updateCartUI);

// Checkout demo
document.getElementById('checkoutBtn')?.addEventListener('click', ()=>{
  if(cart.length===0) return alert('Tu carrito está vacío.');
  alert('Compra simulada: total S/ '+computeTotal().toFixed(2));
  cart=[]; saveCart(); updateCartUI();
});
