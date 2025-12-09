// GSAP Sparkle Starfield — data-driven + mobile-aware + Barba-safe
function initStarfield() {
  const sky = document.getElementById("sky");
  if (!sky) {
    console.warn("Starfield: #sky not found — skipping init.");
    return;
  }

  // Get star count from data attribute
  let starCount = parseInt(sky.dataset.stars || "80", 10);

  // Reduce stars on mobile for clarity + performance
  if (window.matchMedia("(max-width: 600px)").matches) {
    starCount = Math.round(starCount * 0.4); // 40% of normal
  }

  // Clear any previous stars (Barba re-init)
  sky.innerHTML = "";

  // Create GSAP context for cleanup safety
  const ctx = gsap.context(() => {
    const starChar = "✦"; // You can change to: ★ ✧ ✩ ✪ ✦

    for (let i = 0; i < starCount; i++) {
      const el = document.createElement("div");
      el.className = "star";
      el.textContent = starChar;

      // Random positioning inside viewport
      el.style.left = Math.random() * 100 + "vw";
      el.style.top = Math.random() * 100 + "vh";

      // Random sizes (smaller looks more magical)
      const size = gsap.utils.random(6, 18);
      el.style.fontSize = size + "px";

      sky.appendChild(el);

      // Sparkle animation
      function sparkle() {
        gsap
          .timeline()
          .set(el, { opacity: 0, scale: 0.2 })
          .to(el, {
            opacity: gsap.utils.random(0.6, 1),
            scale: gsap.utils.random(1.5, 2.4),
            duration: gsap.utils.random(0.25, 0.45),
            ease: "power2.out"
          })
          .to(el, {
            opacity: 0,
            scale: 0.1,
            duration: gsap.utils.random(0.4, 0.7),
            ease: "power1.inOut",
            delay: gsap.utils.random(0.2, 0.6),
            onComplete: sparkle
          });
      }

      // Offset start time so sparkles are randomized
      gsap.delayedCall(Math.random() * 3, sparkle);
    }
  });

  return ctx;
}

document.addEventListener("DOMContentLoaded", initStarfield);