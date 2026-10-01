export const manifest = {
  screens: {
    scr_c8z923: { name: "Overview", route: "/", position: { "x": 160, "y": 1820 } },
    scr_sakr0f: { name: "My Planning – Private", route: "/my-planning", state: { "area": "private" }, position: { "x": 160, "y": 3800 } },
    scr_aceszw: { name: "My Planning – Bride-side", route: "/my-planning", state: { "area": "side" }, position: { "x": 1560, "y": 3800 } },
    scr_37sat5: { name: "Shared Wedding", route: "/shared", position: { "x": 2960, "y": 3800 } },
    scr_9szsnv: { name: "Tasks", route: "/tasks", position: { "x": 160, "y": 5780 } },
    scr_kb0d0c: { name: "Budget & Expenses", route: "/budget", position: { "x": 160, "y": 9740 } },
    scr_ji5ncn: { name: "Guests & RSVP", route: "/guests", position: { "x": 160, "y": 7760 } },
    scr_9mv2ch: { name: "Seating", route: "/seating", position: { "x": 2960, "y": 7760 } },
    scr_seezer: { name: "Timeline", route: "/timeline", position: { "x": 1560, "y": 9740 } },
    scr_3zkoqq: { name: "Vendors", route: "/vendors", position: { "x": 2960, "y": 9740 } },
    scr_s3oal1: { name: "Home Preparation", route: "/home-preparation", position: { "x": 4360, "y": 9740 } },
    scr_0xt0c1: { name: "Invitations", route: "/invitations", position: { "x": 1560, "y": 7760 } },
    scr_npbedm: { name: "Design System", route: "/design-system", position: { "x": 0, "y": 0 }, isDefaultRow: true }
  },
  sections: {
    sec_a8d3np: { name: "Dashboard", x: 0, y: 1600, width: 1520, height: 1180 },
    sec_dlqdvc: { name: "Planning Workspace", x: 0, y: 3580, width: 4320, height: 1180 },
    sec_hs5gwt: { name: "Tasks", x: 0, y: 5560, width: 1520, height: 1180 },
    sec_4vj0v0: { name: "Guest Management", x: 0, y: 7540, width: 4320, height: 1180 },
    sec_otxvwa: { name: "Planning Details", x: 0, y: 9520, width: 5720, height: 1180 }
  },
  layers: [
  { kind: "screen", id: "scr_npbedm" },
  { kind: "section", id: "sec_a8d3np", children: [
    { kind: "screen", id: "scr_c8z923" }]
  },
  { kind: "section", id: "sec_dlqdvc", children: [
    { kind: "screen", id: "scr_sakr0f" },
    { kind: "screen", id: "scr_aceszw" },
    { kind: "screen", id: "scr_37sat5" }]
  },
  { kind: "section", id: "sec_hs5gwt", children: [
    { kind: "screen", id: "scr_9szsnv" }]
  },
  { kind: "section", id: "sec_4vj0v0", children: [
    { kind: "screen", id: "scr_ji5ncn" },
    { kind: "screen", id: "scr_0xt0c1" },
    { kind: "screen", id: "scr_9mv2ch" }]
  },
  { kind: "section", id: "sec_otxvwa", children: [
    { kind: "screen", id: "scr_kb0d0c" },
    { kind: "screen", id: "scr_seezer" },
    { kind: "screen", id: "scr_3zkoqq" },
    { kind: "screen", id: "scr_s3oal1" }]
  }]

};